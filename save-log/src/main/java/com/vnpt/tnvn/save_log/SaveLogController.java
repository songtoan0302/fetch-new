package com.vnpt.tnvn.save_log;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class SaveLogController {

    @Autowired
    private SaveLogService service;

    @Scheduled(fixedRateString = "#{saveLogConfiguration.scheduleTime}")
    public void start() {
        Logger.getLogger(getClass()).info("Start SAVE LOG at " + new Date());
        service.saveLog();
    }
}
