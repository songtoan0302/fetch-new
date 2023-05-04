package com.vnpt.tnvn.update_profile;

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
public class UpdateProfileApplication {

    public static void main(String[] args) {
        try {
            System.setProperty("log4j.configuration", (new File("./etc/log4j.properties")).toURI().toURL().toString());
            System.setProperty("spring.config.location",
                    (new File("./etc/application.properties")).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            Logger.getLogger("UpdateProfileApplication").error(e.getMessage());
        }
        SpringApplication.run(UpdateProfileApplication.class, args);
    }

}