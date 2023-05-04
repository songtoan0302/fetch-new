package com.vnpt.tnvn.fetch_news;

import com.vnpt.tnvn.fetch_news.common.Utils;
import com.vnpt.tnvn.fetch_news.model.FetchNewsResponse;
import com.vnpt.tnvn.fetch_news.model.News;
import com.vnpt.tnvn.fetch_news.model.NewsRoomSettings;
import com.vnpt.tnvn.fetch_news.model.oracle.*;
import com.vnpt.tnvn.fetch_news.network.ApiService;
import com.vnpt.tnvn.fetch_news.network.ClientService;
import com.vnpt.tnvn.fetch_news.common.Constants;
import com.vnpt.tnvn.fetch_news.repo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FetchNewsService extends ClientService {

    public void fetchBaoThanhNien() {
        for (String category : btnSettings.categories) {
            doGetRequest(btnApiService, buildBtnRequest(category), FetchNewsResponse.class, data -> {
                if (data != null) {
                    handleFetchedNews(editNewsInfo(data.data), Constants.Source.BAO_THANH_NIEN, category);
                }
            });
        }
    }

    public void fetchBaoTienPhong() {
        for (String category : btpSettings.categories) {
            doGetRequest(btpApiService, buildBtpRequest(category), FetchNewsResponse.class, data -> {
                if (data != null) {
                    handleFetchedNews(editNewsInfo(data.data), Constants.Source.BAO_TIEN_PHONG, category);
                }
            });
        }
    }

    public void fetchBaoThieuNien() {
        for (String category : thieunienSettings.categories) {
            doGetRequest(thieunienApiService, buildThieuNienRequest(category), FetchNewsResponse.class, data -> {
                if (data != null) {
                    handleFetchedNews(editNewsInfo(data.data), Constants.Source.BAO_THIEU_NIEN, category);
                }
            });
        }
    }

    private List<News> editNewsInfo(List<News> newsList) {
        for (News news : newsList) {
            news.htmlContent = new StringBuilder()
                .append(news.shortDescription)
                .append("<img style=\"max-width:100%; height:auto; margin-top:1.2em; margin-bottom:1.2em\" ")
                .append("src=\"").append(news.image).append("\"/>")
                .append(news.htmlContent).toString();
        }
        return newsList;
    }

    private void handleFetchedNews(List<News> newsList, String source, String category) {
        Logger.getLogger(getClass()).info("#handleFetchedNews: source=" + source + ", newsList=" + newsList.size());
        for (News news : newsList) {
            news.source = source;
            List<OracleContent> oracleContents = oracleContentRepository.findOracleContentsBySourceAndOriginalId(source, news.newsId);
            if (oracleContents.isEmpty()) {
                Logger.getLogger(getClass()).info("#handleFetchedNews: add news " + news.newsId);
                OracleContent oracleContent = insertNewsInOracle(news);
                updateContentRoleInOracle(oracleContent.id);
                updateContentCategoryInOracle(oracleContent.id, Utils.convertCategoryId(source, category));
                insertNewsInMongo(
                    oracleContent,
                    oracleContentCategoryRepository.findOracleContentCategoriesByContentId(oracleContent.id),
                    oracleContentRoleRepository.findOracleContentRolesByEmbeddedId_ContentId(oracleContent.id)
                );
            } else {
                Logger.getLogger(getClass()).info("handleFetchedNews: update news " + news.newsId);
                OracleContent oracleContent = updateNewsInOracle(oracleContents.get(0), news);
                updateContentRoleInOracle(oracleContent.id);
                updateContentCategoryInOracle(oracleContent.id, Utils.convertCategoryId(source, category));
                updateNewsInMongo(
                    oracleContent,
                    oracleContentCategoryRepository.findOracleContentCategoriesByContentId(oracleContent.id),
                    oracleContentRoleRepository.findOracleContentRolesByEmbeddedId_ContentId(oracleContent.id)
                );
                if (oracleContents.size() > 1) {
                    for (int i = 1; i < oracleContents.size(); i++) {
                        oracleContentRepository.delete(oracleContents.get(i));
                    }
                }
            }
        }
    }

    private OracleContent insertNewsInOracle(News news) {
        return oracleContentRepository.save(Utils.buildOracleContentFromNews(null, news));
    }

    private OracleContent updateNewsInOracle(OracleContent content, News news) {
        return oracleContentRepository.save(Utils.buildOracleContentFromNews(content, news));
    }

    private void updateContentRoleInOracle(long newsId) {
        List<OracleContentRole> contentUnits = oracleContentRoleRepository.findOracleContentRolesByEmbeddedId_ContentId(newsId);
        if (contentUnits == null || contentUnits.isEmpty()) {
            OracleContentRole contentUnit = new OracleContentRole();
            contentUnit.embeddedId = new EmbeddedIdContentRole(newsId, -1L);
            oracleContentRoleRepository.save(contentUnit);
        } else {
            for (OracleContentRole contentUnit : contentUnits) {
                if (contentUnit != null && contentUnit.embeddedId.unitId == null) {
                    contentUnit.embeddedId.unitId = -1L;
                    oracleContentRoleRepository.save(contentUnit);
                }
            }
        }
    }

    private void updateContentCategoryInOracle(long newsId, long categoryId) {
        List<OracleContentCategory> contentCategories =
            oracleContentCategoryRepository.findOracleContentCategoriesByContentId(newsId);
        if (contentCategories == null || contentCategories.isEmpty()) {
            OracleContentCategory contentCategory = new OracleContentCategory();
            contentCategory.contentId = newsId;
            contentCategory.categoryId = categoryId;
            contentCategory.ord = 100L;
            oracleContentCategoryRepository.save(contentCategory);
        } else {
            for (OracleContentCategory category : contentCategories) {
                boolean isChanged = false;
                if (category.ord == null) {
                    isChanged = true;
                    category.ord = 100L;
                }
                if (category.categoryId == null) {
                    isChanged = true;
                    category.categoryId = categoryId;

                }
                if (isChanged) {
                    oracleContentCategoryRepository.save(category);
                }
            }
        }
    }

    private void insertNewsInMongo(OracleContent oracleContent, List<OracleContentCategory> contentCategories, List<OracleContentRole> contentRoles) {
        List<OracleCategory> categories = new ArrayList<>();
        List<OracleRole> roles = new ArrayList<>();
        for (OracleContentCategory contentCategory : contentCategories) {
            OracleCategory category = oracleCategoryRepository.findOracleCategoryById(contentCategory.categoryId);
            if (category != null) {
                category.ord = contentCategory.ord;
                categories.add(category);
            }
        }
        for (OracleContentRole contentRole : contentRoles) {
            OracleRole role = oracleRoleRepository.findOracleRoleById(contentRole.embeddedId.unitId);
            if (role != null) roles.add(role);
        }
        mongoRepository.insert(Utils.buildMongoContentFromOracleContent(null, oracleContent, categories, roles));
    }

    private void updateNewsInMongo(OracleContent oracleContent, List<OracleContentCategory> contentCategories, List<OracleContentRole> contentRoles) {
        List<OracleCategory> categories = new ArrayList<>();
        List<OracleRole> roles = new ArrayList<>();
        for (OracleContentCategory contentCategory : contentCategories) {
            OracleCategory category = oracleCategoryRepository.findOracleCategoryById(contentCategory.categoryId);
            if (category != null) {
                category.ord = contentCategory.ord;
                categories.add(category);
            }
        }
        for (OracleContentRole contentRole : contentRoles) {
            OracleRole role = oracleRoleRepository.findOracleRoleById(contentRole.embeddedId.unitId);
            if (role != null) roles.add(role);
        }
        mongoRepository.save(
            Utils.buildMongoContentFromOracleContent(
                mongoRepository.findMongoContentByNewsId(oracleContent.id), oracleContent, categories, roles
            )
        );
    }

    private String buildBtnRequest(String category) {
        Date date = new Date();
        String startTime = new SimpleDateFormat("yyyyMMdd").format(date) + "000000";
        String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String qry = "category_id=" + category + "&start_time=" + startTime + "&end_time=" + endTime;
        String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(date);
        String secureCode = DigestUtils.md5DigestAsHex((qry + currentTime + btnSettings.privateKey).getBytes());
        qry = qry + "&secure_code=" + secureCode;
        return btnSettings.url + btnSettings.api + "?" + qry;
    }

    private String buildBtpRequest(String category) {
        Date date = new Date();
        String qry = "trans_id=1";
        String startTime = new SimpleDateFormat("yyyyMMdd").format(date) + "000000";
        String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        qry = qry + "&category_id=" + category + "&start_time=" + startTime + "&end_time=" + endTime;
        String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(date);
        String secureCode = DigestUtils.md5DigestAsHex((qry + currentTime + btpSettings.privateKey).getBytes());
        qry = qry + "&secure_code=" + secureCode;
        return btpSettings.url + btpSettings.api + "?" + qry;
    }

    private String buildThieuNienRequest(String category) {
        Date date = new Date();
        String transId = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String startTime = new SimpleDateFormat("yyyyMMdd").format(date) + "000000";
        String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String secureCode = DigestUtils.md5DigestAsHex(
            (transId + category + startTime + endTime + thieunienSettings.privateKey).getBytes()
        );
        return thieunienSettings.url + thieunienSettings.api + "?"
            + "trans_id=" + transId
            + "&category_id=" + category
            + "&start_time=" + startTime
            + "&end_time=" + endTime
            + "&secure_code=" + secureCode;
    }

    @Autowired
    @Qualifier(Constants.Bean.BAO_THANH_NIEN_SETTINGS)
    private NewsRoomSettings btnSettings;

    @Autowired
    @Qualifier(Constants.Bean.BAO_THANH_NIEN_API_SERVICE)
    private ApiService btnApiService;

    @Autowired
    @Qualifier(Constants.Bean.BAO_TIEN_PHONG_SETTINGS)
    private NewsRoomSettings btpSettings;

    @Autowired
    @Qualifier(Constants.Bean.BAO_TIEN_PHONG_API_SERVICE)
    private ApiService btpApiService;

    @Autowired
    @Qualifier(Constants.Bean.BAO_THIEU_NIEN_SETTINGS)
    private NewsRoomSettings thieunienSettings;

    @Autowired
    @Qualifier(Constants.Bean.BAO_THIEU_NIEN_API_SERVICE)
    private ApiService thieunienApiService;

    @Autowired
    private MongoContentRepository mongoRepository;

    @Autowired
    private OracleRoleRepository oracleRoleRepository;

    @Autowired
    private OracleCategoryRepository oracleCategoryRepository;

    @Autowired
    private OracleContentRepository oracleContentRepository;

    @Autowired
    private OracleContentRoleRepository oracleContentRoleRepository;

    @Autowired
    private OracleContentCategoryRepository oracleContentCategoryRepository;
}
