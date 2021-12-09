package com.ns.tradestore.controller;

import com.ns.tradestore.entity.Trade;
import com.ns.tradestore.service.TradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TradeControllerTest {

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeController tradeController;

    @Test
    public void updateTrade() throws Exception {
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);
        Trade savedTrade = tradeController.updateTrade(getMockedTrade());
        assertNotNull(savedTrade);
    }

    private Trade getMockedTrade() {
        Trade trade = new Trade();
        trade.setId(11);
        trade.setCounterparty("Counterparty1");
        trade.setBookId("Book1");
        trade.setMaturityDate(Date.from(Instant.now().plus(Duration.ofDays(2))));
        return trade;
    }

}