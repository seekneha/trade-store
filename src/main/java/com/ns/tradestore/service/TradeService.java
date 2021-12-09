package com.ns.tradestore.service;

import com.ns.tradestore.entity.Trade;
import com.ns.tradestore.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository repo;

    public List<Trade> getAllTrades() {
        return repo.findAll();
    }

    public Trade saveTrade(Trade trade) throws Exception {
        Trade savedTrade = null;
        if (trade.getId() != null) { //Existing Trade
            Trade existingTrade = repo.getById(trade.getId());
            //If the version of the received trade is less than that of the latest existing trade
            if (trade.getVersion()!=null && trade.getVersion() < existingTrade.getVersion()) {
                throw new Exception("Stale Version of Trade Received");
            } else if (trade.getMaturityDate() != null && trade.getMaturityDate().before(new Date())) {
                throw new Exception("Maturity Date Received is less than today");
            } else {
                //Updating the values with the increment in the version
                existingTrade.setBookId(trade.getBookId());
                existingTrade.setCounterparty(trade.getCounterparty());
                existingTrade.setMaturityDate(trade.getMaturityDate());
                existingTrade.setVersion(existingTrade.getVersion() + 1);
                existingTrade.setUpdatedDate(new Date());
                savedTrade = repo.save(existingTrade);
            }
        } else {//It is a new trade creation request
            //Setting only temporal values, expecting all the other data to be sent in the request
            trade.setVersion(1);
            trade.setCreatedDate(new Date());
            trade.setIsExpired(false);
            savedTrade = repo.save(trade);
        }
        return savedTrade;
    }


    public List<Trade> markTradesAsExpired() {
        List<Trade> trades = repo.findAll().stream().filter(t -> t.getIsExpired()==null || !t.getIsExpired())
                .filter(t -> t.getMaturityDate().after(new Date())).collect(Collectors.toList());
        trades.forEach(t -> t.setIsExpired(true));
        List<Trade> savedTrades = repo.saveAll(trades);
        return savedTrades;
    }

}
