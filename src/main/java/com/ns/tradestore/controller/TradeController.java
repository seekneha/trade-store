package com.ns.tradestore.controller;

import com.ns.tradestore.entity.Trade;
import com.ns.tradestore.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService service;

    @PostMapping("/updateTrade")
    public Trade updateTrade(@RequestBody Trade trade) throws Exception
    {
        return service.saveTrade(trade);
    }

    @GetMapping("/trades")
    public List<Trade> getAllTrades() throws Exception
    {
        return service.getAllTrades();
    }

}
