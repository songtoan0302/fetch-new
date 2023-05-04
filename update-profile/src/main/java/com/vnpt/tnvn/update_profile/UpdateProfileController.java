package com.vnpt.tnvn.update_profile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class UpdateProfileController {

    @Autowired
    private UpdateProfileService service;

    @Scheduled(fixedRateString = "#{updateProfileConfiguration.scheduleTime}")
    public void start() {
        Logger.getLogger(getClass()).info("Start UPDATE PROFILE at " + new Date());
        service.scan();
    }
}
