package com.vnpt.tnvn.fetch_news.model.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

public class MongoContentCategory {

    @Field(name = "id")
    public Long id;
    public String title;
    public Long ord;

    public MongoContentCategory(Long id, String title, Long ord) {
        this.id = id;
        this.title = title;
        this.ord = ord;
    }
}