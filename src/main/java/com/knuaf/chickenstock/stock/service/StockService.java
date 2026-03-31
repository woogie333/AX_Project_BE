package com.knuaf.chickenstock.stock.service;

import com.knuaf.chickenstock.stock.dto.StockPrice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class StockService {

    private final Random random = new Random();
    private double lastClose = 75000;

    public StockPrice getLatestPrice(String symbol) {
        double open  = lastClose + (random.nextDouble() * 400 - 200);
        double close = open      + (random.nextDouble() * 1000 - 500);
        double high  = Math.max(open, close) + random.nextDouble() * 300;
        double low   = Math.min(open, close) - random.nextDouble() * 300;
        lastClose = close;

        return new StockPrice(
                symbol,
                (long) open,
                (long) high,
                (long) low,
                (long) close,
                Instant.now().getEpochSecond()
        );
    }
}