package com.vnpt.tnvn.fetch_news.model.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document("Content")
public class MongoContent {
    @Id
    public ObjectId id;
    @Field("id")
    public Long newsId;
    @Field("originalId")
    public String originalId;
    @Field("title")
    public String title;
    @Field("description")
    public String description;
    @Field("info")
    public String info;
    @Field("image")
    public String image;
    @Field("source")
    public String source;
    @Field("author")
    public String author;
    @Field("status")
    public Integer status;
    @Field("createTime")
    public Date createTime;
    @Field("updateTime")
    public Date updateTime;
    @Field("createTimeVal")
    public String createTimeVal;
    @Field("updateTimeVal")
    public String updateTimeVal;
    @Field("viewCount")
    public Long viewCount;
    @Field("onMainPage")
    public Integer onMainPage;
    @Field("asciiTitle")
    public String asciiTitle;
    @Field("webViewLink")
    public String webViewLink;
    @Field("isAll")
    public Integer isAll;
    @Field("hashTags")
    public List<MongoHashtag> hashTags;
    @Field("cateList")
    public List<MongoContentCategory> cateList;
    @Field("roleList")
    public List<MongoContentRole> roleList;
}