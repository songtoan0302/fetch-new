package com.vnpt.tnvn.refresh_token.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "IT_TOKEN")
public class Token {
    @Id
    public ObjectId id;
    @Field(name = "username")
    public String username;
    @Field(name = "accessToken")
    public String accessToken;
    @Field(name = "refreshToken")
    public String refreshToken;
    @Field(name = "idToken")
    public String idToken;
}