package com.vnpt.tnvn.fetch_news.model;

import com.google.gson.annotations.SerializedName;
import org.springframework.util.StringUtils;

public class News {
    public long categoryId;
    public long role;

    @SerializedName("news_id")
    public String newsId;
    @SerializedName("author")
    public String author;
    @SerializedName("title")
    public String title;
    @SerializedName(value = "url", alternate = {"link "})
    public String link;
    @SerializedName(value = "avatar", alternate = {"image"})
    public String image;
    @SerializedName("short_description")
    public String shortDescription;
    @SerializedName("html_content")
    public String htmlContent;
    @SerializedName("public_time")
    public String publicTime;
    @SerializedName("update_time")
    public String updateTime;
    @SerializedName("source")
    public String source;
    @SerializedName("hashtag")
    public String hashTag;
    @SerializedName("avatar_desc")
    public String imageDesc;

    public boolean isValidCategoryId() {
        return !StringUtils.isEmpty(categoryId);
    }

    public boolean isValidAuthor() {
        return !StringUtils.isEmpty(author) && author.getBytes().length <= 100;
    }

    public boolean isValidTitle() {
        return !StringUtils.isEmpty(title) && title.getBytes().length <= 500;
    }

    public boolean isValidLink() {
        return !StringUtils.isEmpty(link) && title.getBytes().length <= 1000;
    }

    public boolean isValidImage() {
        return !StringUtils.isEmpty(image) && image.getBytes().length <= 500;
    }

    public boolean isValidShortDescription() {
        return !StringUtils.isEmpty(shortDescription) && shortDescription.getBytes().length <= 2000;
    }

    public boolean isValidHtmlContent() {
        return !StringUtils.isEmpty(htmlContent);
    }

    public boolean isValidPublicTime() {
        return !StringUtils.isEmpty(publicTime);
    }

    public boolean isValidUpdateTime() {
        return !StringUtils.isEmpty(updateTime);
    }

    public boolean isValidSource() {
        return !StringUtils.isEmpty(source) && source.getBytes().length <= 500;
    }

    public boolean isValidHashtag() {
        return !StringUtils.isEmpty(hashTag) && hashTag.length() <= 255;
    }

    @Override
    public String toString() {
        return "News{" +
            "categoryId=" + categoryId +
            ", role=" + role +
            ", newsId='" + newsId + '\'' +
            ", author='" + author + '\'' +
            ", title='" + title + '\'' +
            ", link='" + link + '\'' +
            ", image='" + image + '\'' +
            ", shortDescription='" + shortDescription + '\'' +
            ", htmlContent='" + htmlContent + '\'' +
            ", publicTime='" + publicTime + '\'' +
            ", updateTime='" + updateTime + '\'' +
            ", source='" + source + '\'' +
            ", hashTag='" + hashTag + '\'' +
            ", imageDesc='" + imageDesc + '\'' +
            '}';
    }
}