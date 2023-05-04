package com.vnpt.tnvn.update_profile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UpdateProfileService {

    private static final String TABLE_ACCOUNT = "ACCOUNT";
    private static final String TABLE_ACCOUNT_ACTIVITIES = "ACCOUNT_ACTIVITIES";

    private final List<String> commands = new ArrayList<>();

    @Autowired
    private UpdateProfileConfiguration configuration;
    @Autowired
    private DataSource dataSource;

    private Connection connection;
    private Statement statement;

    private boolean isUpdating;

    public synchronized void scan() {
        Logger.getLogger(getClass()).info("isUpdating=" + isUpdating);
        if (isUpdating) return;
        isUpdating = true;
        List<File> logFiles = getLogFiles();
        if (logFiles.isEmpty()) {
            isUpdating = false;
            return;
        }
        logFiles.sort((o1, o2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            String o1Name = o1.getName();
            String o2Name = o2.getName();
            try {
                return Long.compare(
                    sdf.parse(o1Name.substring(o1Name.length() - 13)).getTime(),
                    sdf.parse(o2Name.substring(o2Name.length() - 13)).getTime()
                );
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        });
        try {
            initConnection();
            for (File logFile : logFiles) {
                scanFile(logFile);
                moveFileToScannedFolder(logFile);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass()).error("connect: " + e.getMessage());
        } finally {
            releaseConnection();
        }
        isUpdating = false;
    }

    private List<File> getLogFiles() {
        List<File> logFiles = new ArrayList<>();
        File directory = new File(configuration.getDirectoryPath());
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File logFile : files) {
                    if (shouldSaveLogFile(logFile.getName(), configuration.getPrefix().split(","))) {
                        logFiles.add(logFile);
                    }
                }
            }
        }
        if (logFiles.size() > 1) {
            logFiles.sort(Comparator.comparingLong(File::lastModified));
        }
        return logFiles;
    }

    private boolean shouldSaveLogFile(String filename, String[] prefixArray) {
        if (!StringUtils.isEmpty(filename)) {
            for (String prefix : prefixArray) {
                if (filename.contains(prefix)) return true;
            }
        }
        return false;
    }

    private void initConnection() throws SQLException {
        connection = dataSource.getConnection();
        statement = connection.createStatement();
    }

    private void releaseConnection() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass()).error("close: " + e.getMessage());
        }
    }

    private void scanFile(File file) {
        Logger.getLogger(getClass()).info("START update file " + file.getName());
        try (Scanner scanner = new Scanner(file)) {
            int batchSize = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (addCommandToBatch(line.split(",", -1))) {
                    batchSize++;
                    if (batchSize == 50) {
                        executeBatch();
                        batchSize = 0;
                    }
                }
            }
            if (batchSize > 0) {
                executeBatch();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.getLogger(getClass()).error("scan: " + e.getMessage());
        }
        Logger.getLogger(getClass()).info("END update file " + file.getName());
    }

    private void executeBatch() {
        try {
            int[] results = statement.executeBatch();
            for (int i : results) {
                if (i == Statement.EXECUTE_FAILED) {
                    Logger.getLogger(getClass()).error("executeBatch: ERROR IN SQL: " + commands.get(i));
                }
            }
            clearCommands();
        } catch (SQLException e) {
            Logger.getLogger(getClass()).error("executeBatch: " + e.getMessage());
        }
    }

    private boolean addCommandToBatch(String[] params) {
        if (StringUtils.isEmpty(params[0])) return false;
        String sqlCommand = null;
        try {
            switch (params[0]) {
                case TABLE_ACCOUNT:
                    if (params.length == 10 && StringUtils.hasLength(params[9])) {
                        sqlCommand = buildAccountCommandV1(params);
                    } else if (params.length == 24 && StringUtils.hasLength(params[9])) {
                        sqlCommand = buildAccountCommandV2(params);
                    }
                    break;
                case TABLE_ACCOUNT_ACTIVITIES:
                    if (params.length == 11 && StringUtils.hasLength(params[10])) {
                        sqlCommand = buildAccountActivitiesCommandV1(params);
                    } else if (params.length == 13 && StringUtils.hasLength(params[12])) {
                        sqlCommand = buildAccountActivitiesCommandV2(params);
                    }
                    break;
                default:
                    break;
            }
            if (StringUtils.hasLength(sqlCommand)) {
                addCommand(sqlCommand);
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass()).error("addCommandToBatch: sqlCommand=" + sqlCommand + "\n" + e.getMessage());
        }
        return false;
    }

    private void addCommand(String sqlCommand) throws SQLException {
        commands.add(sqlCommand);
        statement.addBatch(sqlCommand);
    }

    private void clearCommands() throws SQLException {
        commands.clear();
        statement.clearBatch();
    }

    private String buildAccountCommandV1(String[] params) {
        String content = (StringUtils.isEmpty(params[1]) ? "" : "ADDRESS='" + params[1] + "'")
            + (StringUtils.isEmpty(params[2]) ? "" : ",FULLNAME='" + params[2] + "'")
            + (StringUtils.isEmpty(params[3]) ? "" : ",PHONE='" + params[3] + "'")
            + (StringUtils.isEmpty(params[4]) ? "" : ",GENDER='" + params[4] + "'")
            + (StringUtils.isEmpty(params[5]) ? "" : ",CAT_QUAN_ID='" + params[5] + "'")
            + (StringUtils.isEmpty(params[6]) ? "" : ",CAT_TINH_ID='" + params[6] + "'")
            + (StringUtils.isEmpty(params[7]) ? "" : ",CAT_XA_ID='" + params[7] + "'")
            + (StringUtils.isEmpty(params[8]) ? "" : ",BIRTHDAY=to_date('" + params[8] + "','DDMMYYYY')")
            + ",UPDATE_TIME=to_date('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "','YYYY-MM-DD HH24:MI:SS')";
        if (content.charAt(0) == ',') {
            content = content.substring(1);
        }
        return "UPDATE ACCOUNT SET " + content + " WHERE ID = '" + params[9] + "'";
    }

    private String buildAccountCommandV2(String[] params) {
        String content = (StringUtils.isEmpty(params[1]) ? "" : "ADDRESS='" + params[1] + "'")
            + (StringUtils.isEmpty(params[2]) ? "" : ",FULLNAME='" + params[2] + "'")
            + (StringUtils.isEmpty(params[3]) ? "" : ",PHONE='" + params[3] + "'")
            + (StringUtils.isEmpty(params[4]) ? "" : ",GENDER='" + params[4] + "'")
            + (StringUtils.isEmpty(params[5]) ? "" : ",CAT_QUAN_ID='" + params[5] + "'")
            + (StringUtils.isEmpty(params[6]) ? "" : ",CAT_TINH_ID='" + params[6] + "'")
            + (StringUtils.isEmpty(params[7]) ? "" : ",CAT_XA_ID='" + params[7] + "'")
            + (StringUtils.isEmpty(params[8]) ? "" : ",BIRTHDAY=to_date('" + params[8] + "','DDMMYYYY')")
            + (StringUtils.isEmpty(params[10]) ? "" : ",QUEQUAN_QUAN_ID='" + params[10] + "'")
            + (StringUtils.isEmpty(params[11]) ? "" : ",QUEQUAN_TINH_ID='" + params[11] + "'")
            + (StringUtils.isEmpty(params[12]) ? "" : ",QUEQUAN_XA_ID='" + params[12] + "'")
            + (StringUtils.isEmpty(params[13]) ? "" : ",CCCD_ID='" + params[13] + "'")
            + (StringUtils.isEmpty(params[14]) ? "" : ",CCCD_NGAY_CAP=to_date('" + params[14] + "','DDMMYYYY')")
            + (StringUtils.isEmpty(params[15]) ? "" : ",CCCD_NOI_CAP='" + params[15] + "'")
            + (StringUtils.isEmpty(params[16]) ? "" : ",CHUC_VU_DOAN_ID='" + params[16] + "'")
            + (StringUtils.isEmpty(params[17]) ? "" : ",DANTOC_ID='" + params[17] + "'")
            + (StringUtils.isEmpty(params[18]) ? "" : ",TON_GIAO='" + params[18] + "'")
            + (StringUtils.isEmpty(params[19]) ? "" : ",VAN_HOA_ID='" + params[19] + "'")
            + (StringUtils.isEmpty(params[20]) ? "" : ",CHUYEN_MON_ID='" + params[20] + "'")
            + (StringUtils.isEmpty(params[21]) ? "" : ",TIN_HOC_ID='" + params[21] + "'")
            + (StringUtils.isEmpty(params[22]) ? "" : ",NGOAI_NGU_ID='" + params[22] + "'")
            + (StringUtils.isEmpty(params[23]) ? "" : ",CHINH_TRI_ID='" + params[23] + "'")
            + ",UPDATE_TIME=to_date('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "','YYYY-MM-DD HH24:MI:SS')";
        if (content.charAt(0) == ',') {
            content = content.substring(1);
        }
        return "UPDATE ACCOUNT SET " + content + " WHERE ID = '" + params[9] + "'";
    }

    private String buildAccountActivitiesCommandV1(String[] params) {
        String content = (StringUtils.isEmpty(params[1]) ? "" : "DOAN_CAP1='" + params[1] + "'")
            + (StringUtils.isEmpty(params[2]) ? "" : ",DOAN_CAP2='" + params[2] + "'")
            + (StringUtils.isEmpty(params[3]) ? "" : ",DOAN_CAP3='" + params[3] + "'")
            + (StringUtils.isEmpty(params[4]) ? "" : ",HOISINHVIEN_CAP1='" + params[4] + "'")
            + (StringUtils.isEmpty(params[5]) ? "" : ",HOISINHVIEN_CAP2='" + params[5] + "'")
            + (StringUtils.isEmpty(params[6]) ? "" : ",HOISINHVIEN_CAP3='" + params[6] + "'")
            + (StringUtils.isEmpty(params[7]) ? "" : ",HOITHANHNIEN_CAP1='" + params[7] + "'")
            + (StringUtils.isEmpty(params[8]) ? "" : ",HOITHANHNIEN_CAP2='" + params[8] + "'")
            + (StringUtils.isEmpty(params[9]) ? "" : ",HOITHANHNIEN_CAP3='" + params[9] + "'");
        if (content.length() > 0 && content.charAt(0) == ',') {
            content = content.substring(1);
        }
        return "UPDATE ACCOUNT_ACTIVITIES SET " + content + " WHERE ACCOUNT_ID = '" + params[10] + "'";
    }

    private String buildAccountActivitiesCommandV2(String[] params) {
        String content = (StringUtils.isEmpty(params[1]) ? "" : "DOAN_CAP1='" + params[1] + "'")
            + (StringUtils.isEmpty(params[2]) ? "" : ",DOAN_CAP2='" + params[2] + "'")
            + (StringUtils.isEmpty(params[3]) ? "" : ",DOAN_CAP3='" + params[3] + "'")
            + (StringUtils.isEmpty(params[4]) ? "" : ",HOISINHVIEN_CAP1='" + params[4] + "'")
            + (StringUtils.isEmpty(params[5]) ? "" : ",HOISINHVIEN_CAP2='" + params[5] + "'")
            + (StringUtils.isEmpty(params[6]) ? "" : ",HOISINHVIEN_CAP3='" + params[6] + "'")
            + (StringUtils.isEmpty(params[7]) ? "" : ",HOITHANHNIEN_CAP1='" + params[7] + "'")
            + (StringUtils.isEmpty(params[8]) ? "" : ",HOITHANHNIEN_CAP2='" + params[8] + "'")
            + (StringUtils.isEmpty(params[9]) ? "" : ",HOITHANHNIEN_CAP3='" + params[9] + "'")
            + (StringUtils.isEmpty(params[10]) ? "" : ",DOAN_CAP4='" + params[10] + "'")
            + (StringUtils.isEmpty(params[11]) ? "" : ",DOAN_CAP5='" + params[11] + "'");
        if (content.length() > 0 && content.charAt(0) == ',') {
            content = content.substring(1);
        }
        return "UPDATE ACCOUNT_ACTIVITIES SET " + content + " WHERE ACCOUNT_ID = '" + params[12] + "'";
    }

    private void moveFileToScannedFolder(File file) {
        try {
            File scanFolder = new File(file.getParentFile().getAbsolutePath() + "/scanned/");
            if (!scanFolder.exists() && !scanFolder.mkdir()) {
                return;
            }
            Files.move(
                Paths.get(file.getAbsolutePath()),
                Paths.get(scanFolder.getAbsolutePath() + "/" + file.getName())
            );
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("move: " + e.getMessage());
        }
    }
}
