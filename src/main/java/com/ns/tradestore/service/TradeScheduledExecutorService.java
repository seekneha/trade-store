package com.ns.tradestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class TradeScheduledExecutorService {

    @Autowired
    private TradeService tradeService;

    private final java.util.concurrent.ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startScheduledJob() {
        //Scheduler that will run daily ata particular time.
        //This will check whether maturity data has already crossed and will mark such trades expired

        Runnable setTradesAsExpiredTask = () -> tradeService.markTradesAsExpired();

        final ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(setTradesAsExpiredTask, 0, 1, TimeUnit.DAYS);
    }

}
