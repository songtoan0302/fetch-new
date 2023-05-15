package com.vnpt.tnvn.save_log;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.net.MalformedURLException;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class SaveLogApplication {

    public static void main(String[] args) {
        try {
            System.setProperty("log4j.configuration", (new File("C:\\Users\\nguye\\Downloads\\Vivas\\TNVN\\process_dongbo_bao\\save-log\\src\\main\\resources\\log4j.properties")).toURI().toURL().toString());
            System.setProperty("spring.config.location",
                    (new File("C:\\Users\\nguye\\Downloads\\Vivas\\TNVN\\process_dongbo_bao\\save-log\\src\\main\\resources\\application.properties")).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            Logger.getLogger("SaveLogApplication").error(e.getMessage());
        }
        SpringApplication.run(SaveLogApplication.class, args);
    }

}
