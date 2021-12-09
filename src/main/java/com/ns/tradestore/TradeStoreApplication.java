package com.ns.tradestore;

import com.ns.tradestore.service.TradeScheduledExecutorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeStoreApplication {

	public static void main(String[] args) {
		TradeScheduledExecutorService tradeScheduledExecutorService = new TradeScheduledExecutorService();
		tradeScheduledExecutorService.startScheduledJob();
		SpringApplication.run(TradeStoreApplication.class, args);
	}

}
