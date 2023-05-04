package com.vnpt.tnvn.send_email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sendemail")
public class SendEmailConfiguration {

    private long scheduleTime;
    private String smtpHost;
    private String smtpPort;
    private String senderEmail;
    private String senderPassword;

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }
}
