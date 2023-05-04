package com.vnpt.tnvn.refresh_token;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "refreshtoken")
public class RefreshTokenConfiguration {

    private long scheduleTime;
    private String baseUrl;
    private String refreshApi;
    private String clientId;
    private String clientSecret;

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRefreshApi() {
        return refreshApi;
    }

    public void setRefreshApi(String refreshApi) {
        this.refreshApi = refreshApi;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
