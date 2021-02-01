package com.kktt.jesus.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GoTenProductScheduler {

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
//    @Scheduled(cron = "0 10 0 * * ?", zone = "GMT+8")
    public void runSyncProduct() {



    }






}