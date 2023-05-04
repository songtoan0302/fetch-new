package com.vnpt.tnvn.update_profile;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "updateprofile")
public class UpdateProfileConfiguration {

    private long scheduleTime;
    private String directoryPath;
    private String prefix;

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
