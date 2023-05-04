package com.vnpt.tnvn.fetch_news.common;

import com.vnpt.tnvn.fetch_news.model.News;
import com.vnpt.tnvn.fetch_news.model.mongo.MongoContent;
import com.vnpt.tnvn.fetch_news.model.mongo.MongoContentCategory;
import com.vnpt.tnvn.fetch_news.model.mongo.MongoContentRole;
import com.vnpt.tnvn.fetch_news.model.mongo.MongoHashtag;
import com.vnpt.tnvn.fetch_news.model.oracle.OracleCategory;
import com.vnpt.tnvn.fetch_news.model.oracle.OracleContent;
import com.vnpt.tnvn.fetch_news.model.oracle.OracleRole;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static long convertCategoryId(String source, String category) {
        long categoryId = -1;
        if (Constants.Source.BAO_THANH_NIEN.equals(source)) {
            switch (category) {
                case "12":
                    categoryId = Constants.CategoryId.CONG_NGHE;
                    break;
                case "69":
                    categoryId = Constants.CategoryId.TIEU_DIEM;
                    break;
                default:
                    break;
            }
        } else if (Constants.Source.BAO_TIEN_PHONG.equals(source)) {
            switch (category) {
                case "4":
                    categoryId = Constants.CategoryId.TIEU_DIEM;
                    break;
                default:
                    break;
            }
        } else if (Constants.Source.BAO_THIEU_NIEN.equals(source)) {
            categoryId = Constants.CategoryId.GIOI_TRE;
        }
        return categoryId;
    }

    public static OracleContent buildOracleContentFromNews(OracleContent content, News news) {
        boolean isInsert = false;
        if (content == null) {
            content = new OracleContent();
            isInsert = true;
        }
        if (news.isValidAuthor()) {
            content.author = news.author;
        }
        if (news.isValidTitle()) {
            content.title = news.title;
        }
        if (news.isValidLink()) {
            content.webViewLink = news.link;
        }
        if (news.isValidImage()) {
            content.image = news.image;
        }
        if (news.isValidShortDescription()) {
            content.description = news.shortDescription;
        }
        if (news.isValidHtmlContent()) {
            content.info = news.htmlContent;
        }
        if (content.createTime == null) {
            try {
                content.createTime = news.isValidPublicTime()
                    ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(news.publicTime) : new Date();
            } catch (ParseException e) {
                Logger.getLogger("buildOracleContentFromNews").error(e.getMessage());
            }
        }
        if (content.updateTime == null) {
            try {
                content.updateTime = news.isValidUpdateTime()
                    ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(news.updateTime) : new Date();
            } catch (ParseException e) {
                Logger.getLogger("buildOracleContentFromNews").error(e.getMessage());
            }
        }
        if (news.isValidSource()) {
            content.source = news.source;
        }
        if (news.isValidHashtag()) {
            content.hashtag = news.hashTag;
        }
        if (isInsert) {
            content.isAll = 1;
            content.status = 1;
            content.syncStatus = 0;
            content.onMainPage = 1;
            content.viewCount = 0;
        }
        content.originalId = news.newsId;
        return content;
    }

    public static MongoContent buildMongoContentFromOracleContent(
            MongoContent mongoContent,
            OracleContent oracleContent,
            List<OracleCategory> categories,
            List<OracleRole> roles
    ) {
        if (mongoContent == null) mongoContent = new MongoContent();
        mongoContent.newsId = oracleContent.id;
        mongoContent.originalId = oracleContent.originalId;
        mongoContent.title = oracleContent.title;
        mongoContent.asciiTitle = new VNCharacterUtils().removeSpecialCharacter(oracleContent.title).toUpperCase();
        mongoContent.description = oracleContent.description;
        mongoContent.info = oracleContent.info;
        mongoContent.image = oracleContent.image;
        mongoContent.author = oracleContent.author;
        mongoContent.webViewLink = oracleContent.webViewLink;
        mongoContent.status = oracleContent.status;
        mongoContent.isAll = oracleContent.isAll;
        mongoContent.onMainPage = oracleContent.onMainPage;
        mongoContent.createTime = oracleContent.createTime;
        if (oracleContent.createTime != null) {
            mongoContent.createTimeVal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oracleContent.createTime);
        }
        mongoContent.updateTime = oracleContent.updateTime;
        if (oracleContent.updateTime != null) {
            mongoContent.updateTimeVal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oracleContent.updateTime);
        }
        mongoContent.source = oracleContent.source;
        if (oracleContent.hashtag != null) {
            String[] newHashtags = oracleContent.hashtag.split(",");
            if (newHashtags.length > 0) {
                mongoContent.hashTags = new ArrayList<>();
                for (String hashtag : newHashtags) {
                    mongoContent.hashTags.add(new MongoHashtag(hashtag));
                }
            }
        }
        if (categories != null && !categories.isEmpty()) {
            mongoContent.cateList = new ArrayList<>();
            for (OracleCategory category : categories) {
                mongoContent.cateList.add(new MongoContentCategory(category.id, category.name, category.ord));
            }
        }
        if (roles != null && !roles.isEmpty()) {
            mongoContent.roleList = new ArrayList<>();
            for (OracleRole role : roles) {
                mongoContent.roleList.add(new MongoContentRole(role.id, role.name));
            }
        }
        return mongoContent;
    }
}
