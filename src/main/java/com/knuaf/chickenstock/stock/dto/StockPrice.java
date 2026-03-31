package com.knuaf.chickenstock.stock.dto;


public record StockPrice(
        String symbol,
        long open,
        long high,
        long low,
        long close,
        long time
) {}