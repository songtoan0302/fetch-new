package com.vnpt.tnvn.fetch_news.model.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

public class MongoContentRole {

    @Field(name = "id")
    public Long id;
    public String unitName;

    public MongoContentRole(Long id, String unitName) {
        this.id = id;
        this.unitName = unitName;
    }
}