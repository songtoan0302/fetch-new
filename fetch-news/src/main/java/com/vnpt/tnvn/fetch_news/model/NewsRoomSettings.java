package com.vnpt.tnvn.fetch_news.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsRoomSettings {
    @SerializedName("name")
    public String name;
    @SerializedName("url")
    public String url;
    @SerializedName("api")
    public String api;
    @SerializedName("private_key")
    public String privateKey;
    @SerializedName("categories")
    public List<String> categories;
}