package com.vnpt.tnvn.fetch_news;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class FetchNewsController {

    @Autowired
    private FetchNewsService service;

    @Scheduled(fixedRateString = "#{@fetchNewsConfiguration.scheduleTime}")
    public synchronized void start() {
        Logger.getLogger(getClass()).info("Start FETCHING at " + new Date());
        service.fetchBaoThanhNien();
        service.fetchBaoTienPhong();
        service.fetchBaoThieuNien();
    }
}
