package com.vnpt.tnvn.save_log;

import com.vnpt.tnvn.save_log.model.MongoLogEvent;
import com.vnpt.tnvn.save_log.model.OracleLogEvent;
import com.vnpt.tnvn.save_log.repo.MongoLogEventRepository;
import com.vnpt.tnvn.save_log.repo.OracleLogEventRepository;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class SaveLogService {

    @Autowired
    private SaveLogConfiguration configuration;
    @Autowired
    private MongoLogEventRepository mongoLogEventRepository;
    @Autowired
    private OracleLogEventRepository oracleLogEventRepository;

    private boolean isSaving;

    public synchronized void saveLog() {
        Logger.getLogger(getClass()).info("isSaving=" + isSaving);
        if (isSaving) return;
        List<File> logFiles = getLogFiles();
        if (logFiles.isEmpty()) return;
        isSaving = true;
        for (File logFile : logFiles) {
            try (Scanner scanner = new Scanner(logFile)) {
                while (scanner.hasNextLine()) {
                    OracleLogEvent oracleLogEvent = insertOracleLogEvent(scanner.nextLine());
                    if (oracleLogEvent != null) {
                        insertMongoLogEvent(oracleLogEvent);
                    }
                }
            } catch (FileNotFoundException e) {
                Logger.getLogger(getClass()).error("saveLog: " + e.getMessage());
                continue;
            }
            try {
                Files.delete(Paths.get(logFile.getAbsolutePath()));
            } catch (IOException e) {
                Logger.getLogger(getClass()).error("saveLog: " + e.getMessage());
            }
        }
        isSaving = false;
    }

    private OracleLogEvent insertOracleLogEvent(String data) {
        try {
            OracleLogEvent logEvent = new OracleLogEvent();
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("requestMethod")) {
                logEvent.api = jsonObject.getString("requestMethod");
            }
            if (jsonObject.has("requestBody")) {
                logEvent.request = new JSONObject(jsonObject.getString("requestBody")).toString();
            }
            if (jsonObject.has("responseBody")) {
                logEvent.response = new JSONObject(jsonObject.getString("responseBody")).getString("message");
            }
            if (jsonObject.has("startTime")) {
                logEvent.startTime = new Timestamp(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("startTime")).getTime()
                );
            }
            if (jsonObject.has("insertTime")) {
                logEvent.createTime = new Timestamp(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("insertTime")).getTime()
                );
            }
            if (logEvent.createTime != null && logEvent.startTime != null) {
                logEvent.deltaTime = logEvent.createTime.getTime() - logEvent.startTime.getTime();
            }
            if (jsonObject.has("userName")) {
                logEvent.username = jsonObject.getString("userName");
            }
            logEvent = oracleLogEventRepository.save(logEvent);
            Thread.sleep(30);
            return logEvent;
        } catch (JSONException | ParseException | InterruptedException e) {
            Logger.getLogger(getClass()).error("insertOracleLogEvent: " + e.getMessage() + "\n" + data);
        }
        return null;
    }

    private void insertMongoLogEvent(OracleLogEvent oracleLogEvent) {
        MongoLogEvent logEvent = new MongoLogEvent();
        logEvent.logId = oracleLogEvent.id;
        logEvent.api = oracleLogEvent.api;
        logEvent.request = oracleLogEvent.request;
        logEvent.response = oracleLogEvent.response;
        logEvent.startTime = oracleLogEvent.startTime;
        logEvent.createTime = oracleLogEvent.createTime;
        logEvent.deltaTime = oracleLogEvent.deltaTime;
        logEvent.username = oracleLogEvent.username;
        mongoLogEventRepository.save(logEvent);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Logger.getLogger(getClass()).error("insertMongoLogEvent: " + e.getMessage());
        }
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
}
