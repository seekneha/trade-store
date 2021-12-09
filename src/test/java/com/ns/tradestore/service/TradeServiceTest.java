package com.ns.tradestore.service;

import com.ns.tradestore.entity.Trade;
import com.ns.tradestore.repository.TradeRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    public void updateTrade_Positive() throws Exception {
        when(tradeRepository.getById(Mockito.any())).thenReturn(getMockedTrade());
        when(tradeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        Trade trade = getMockedTrade();
        Trade savedTrade = tradeService.saveTrade(trade);
        assertNotNull(savedTrade);
        assertEquals(2, savedTrade.getVersion());
    }

    private Trade getMockedTrade() {
        Trade trade = new Trade();
        trade.setId(11);
        trade.setVersion(1);
        trade.setCounterparty("Counterparty1");
        trade.setBookId("Book1");
        trade.setMaturityDate(Date.from(Instant.now().plus(Duration.ofDays(2))));
        return trade;
    }

    @Test(expected = Exception.class)
    public void saveTrade_StaleVersion() throws Exception {
        Trade trade = getMockedTrade();
        trade.setVersion(2);
        //Existing trade returned from db has version = 2
        when(tradeRepository.getById(Mockito.any())).thenReturn(trade);
        when(tradeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //Requested trade to be updated has version =1 (Stale), hence added expected exception in the @Test annotation
        tradeService.saveTrade(getMockedTrade());
    }

    @Test(expected = Exception.class)
    public void saveTrade_MaturityDateTest() throws Exception {

        when(tradeRepository.getById(Mockito.any())).thenReturn(getMockedTrade());
        when(tradeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Trade trade = getMockedTrade();
        trade.setMaturityDate(Date.from(Instant.now().minus(Duration.ofDays(2))));
        //Requested trade to be updated has MaturityDate less than today, hence added expected exception in the @Test annotation
        tradeService.saveTrade(trade);
    }

    @Test
    public void createTrade_Positive() throws Exception {
        Trade trade = getMockedTrade();
        trade.setId(null);
        trade.setVersion(null);
        when(tradeRepository.getById(Mockito.any())).thenReturn(trade);
        Trade tradeWithId = getMockedTrade();tradeWithId.setId(1);
        when(tradeRepository.save(any())).thenReturn(tradeWithId);

        Trade savedTrade = tradeService.saveTrade(trade);
        assertNotNull(savedTrade);
        assertEquals(1, savedTrade.getVersion());
    }

    @Test
    public void markTradesAsExpired() {
        Trade t1 = getMockedTrade();
        t1.setMaturityDate(Date.from(Instant.now().minus(Duration.ofDays(1))));
        Trade t2 = getMockedTrade();
        t2.setMaturityDate(Date.from(Instant.now().plus(Duration.ofDays(1))));
        Trade t3 = getMockedTrade();
        t3.setMaturityDate(Date.from(Instant.now()));
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(t1,t2,t3));
        when(tradeRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        List<Trade> savedTrades = tradeService.markTradesAsExpired();
        assertNotNull(savedTrades);
        //Expects only one to be marked as Expired, because in our list there is only one trade
        //whose MaturityDate has crossed current date
        assertEquals(1, savedTrades.size());
    }

}