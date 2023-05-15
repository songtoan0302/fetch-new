package com.vnpt.tnvn.fetch_news.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vnpt.tnvn.fetch_news.model.NewsRoomSettings;
import com.vnpt.tnvn.fetch_news.network.ApiService;
import com.vnpt.tnvn.fetch_news.common.Constants;
import okhttp3.OkHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Configuration
public class NetworkConfiguration {

  private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
      .build();

  private final GsonConverterFactory gsonConverter = GsonConverterFactory.create(new GsonBuilder().setLenient().create());

  @Autowired
  private FetchNewsConfiguration fetchNewsConfiguration;

  @Bean(Constants.Bean.BAO_THANH_NIEN_API_SERVICE)
  @Autowired
  @Qualifier(Constants.Bean.BAO_THANH_NIEN_SETTINGS)
  public ApiService initBtnApiService(NewsRoomSettings btnSettings) {
    return new Retrofit.Builder()
        .baseUrl(btnSettings.url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()
        .create(ApiService.class);
  }

  @Bean(Constants.Bean.BAO_TIEN_PHONG_API_SERVICE)
  @Autowired
  @Qualifier(Constants.Bean.BAO_TIEN_PHONG_SETTINGS)
  public ApiService initBtpApiService(NewsRoomSettings btpSettings) {
    return new Retrofit.Builder().baseUrl(btpSettings.url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()
        .create(ApiService.class);
  }

  @Bean(Constants.Bean.BAO_THIEU_NIEN_API_SERVICE)
  @Autowired
  @Qualifier(Constants.Bean.BAO_THIEU_NIEN_SETTINGS)
  public ApiService initThieuNienApiService(NewsRoomSettings thieunienSettings) {
    return new Retrofit.Builder().baseUrl(thieunienSettings.url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()
        .create(ApiService.class);
  }

  @Bean(Constants.Bean.BAO_THANH_NIEN_SETTINGS)
  @Autowired
  @Qualifier(Constants.Bean.NEWS_ROOM_JSON)
  public NewsRoomSettings initBtnSettings(JSONObject jsonObject) {
    JSONArray configs = jsonObject.getJSONArray("update_news");
    for (int i = 0; i < configs.length(); i++) {
      JSONObject newsRoomsInfo = configs.getJSONObject(i);
      if ("baothanhnien".equals(newsRoomsInfo.getString("name"))) {
        return new Gson().fromJson(newsRoomsInfo.toString(), NewsRoomSettings.class);
      }
    }
    return null;
  }

  @Bean(Constants.Bean.BAO_TIEN_PHONG_SETTINGS)
  @Autowired
  @Qualifier(Constants.Bean.NEWS_ROOM_JSON)
  public NewsRoomSettings initBtpSettings(JSONObject jsonObject) {
    JSONArray configs = jsonObject.getJSONArray("update_news");
    for (int i = 0; i < configs.length(); i++) {
      JSONObject newsRoomsInfo = configs.getJSONObject(i);
      if ("baotienphong".equals(newsRoomsInfo.getString("name"))) {
        return new Gson().fromJson(newsRoomsInfo.toString(), NewsRoomSettings.class);
      }
    }
    return null;
  }

  @Bean(Constants.Bean.BAO_THIEU_NIEN_SETTINGS)
  @Autowired
  @Qualifier(Constants.Bean.NEWS_ROOM_JSON)
  public NewsRoomSettings initThieuNienSettings(JSONObject jsonObject) {
    JSONArray configs = jsonObject.getJSONArray("update_news");
    for (int i = 0; i < configs.length(); i++) {
      JSONObject newsRoomsInfo = configs.getJSONObject(i);
      if ("baothieunien".equals(newsRoomsInfo.getString("name"))) {
        return new Gson().fromJson(newsRoomsInfo.toString(), NewsRoomSettings.class);
      }
    }
    return null;
  }

  @Bean(name = Constants.Bean.NEWS_ROOM_JSON)
  public JSONObject getJsonObjectFromFileConfig() {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      Stream<String> stream = Files.lines(Paths.get(fetchNewsConfiguration.getFileConfigPath()), Charset.defaultCharset());
      stream.forEach(s -> {
        stringBuilder.append(s).append("\n");
      });
      stream.close();
    } catch (IOException e) {
      Logger.getLogger(getClass()).error(e.getMessage());
    }
    return new JSONObject(stringBuilder.toString());
  }
}
