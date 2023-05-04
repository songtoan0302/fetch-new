package com.vnpt.tnvn.save_log.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;

@Document("TNVN_EVENT")
public class MongoLogEvent {
    @Id
    public ObjectId id;
    @Field(name = "logId")
    public Long logId;
    @Field(name = "ACTION")
    public String action;
    @Field(name = "API")
    public String api;
    @Field(name = "CREATE_TIME")
    public Timestamp createTime;
    @Field(name = "DELTATIME")
    public Long deltaTime;
    @Field(name = "REQUEST")
    public String request;
    @Field(name = "RESPONSE")
    public String response;
    @Field(name = "TIME")
    public Timestamp startTime;
    @Field(name = "USERNAME")
    public String username;
}
