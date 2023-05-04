package com.vnpt.tnvn.send_email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class SendEmailController {

    @Autowired
    private SendEmailService service;

    @Scheduled(fixedRateString = "#{sendEmailConfiguration.scheduleTime}")
    public void start() {
        Logger.getLogger(getClass()).info("Start SEND EMAIL at " + new Date());
        service.sendEmail();
    }
}
