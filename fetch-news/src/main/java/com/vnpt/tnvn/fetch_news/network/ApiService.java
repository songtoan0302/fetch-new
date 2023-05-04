package com.vnpt.tnvn.fetch_news.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface ApiService {
    @GET
    Call<ResponseBody> doGet(@Url String url);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> doPostFormUrlEncoded(@Url String url, @FieldMap Map<String, String> params);
}
