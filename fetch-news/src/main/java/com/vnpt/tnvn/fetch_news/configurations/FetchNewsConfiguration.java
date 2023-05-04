package com.vnpt.tnvn.fetch_news.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fetchnews")
public class FetchNewsConfiguration {

    private long scheduleTime;
    private String fileConfigPath;

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getFileConfigPath() {
        return fileConfigPath;
    }

    public void setFileConfigPath(String fileConfigPath) {
        this.fileConfigPath = fileConfigPath;
    }
}