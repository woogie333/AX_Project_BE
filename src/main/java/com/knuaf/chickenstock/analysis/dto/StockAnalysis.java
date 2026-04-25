package com.knuaf.chickenstock.analysis.dto;

import java.util.List;
import java.util.Map;

public record StockAnalysis(
        String ticker,
        String name,
        String analysisDate,
        String signal,
        String label,
        double totalScore,
        double confidence,
        Map<String, Double> axisScores,
        List<InsiderTrade> insiderTrades
) {
    public record InsiderTrade(
            String date,
            String name,
            String type,
            int shares,
            double value
    ) {}
}