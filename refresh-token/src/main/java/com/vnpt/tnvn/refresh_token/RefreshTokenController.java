package com.vnpt.tnvn.refresh_token;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService service;

    @Scheduled(fixedRateString = "#{refreshTokenConfiguration.scheduleTime}")
    public void start() {
        Logger.getLogger(getClass()).info("Start REFRESH TOKEN at " + new Date());
        service.refresh();
    }
}
