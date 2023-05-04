package com.vnpt.tnvn.fetch_news.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchNewsResponse {
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_desc")
    public String errorDesc;
    @SerializedName("data")
    public List<News> data;
}