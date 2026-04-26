package com.knuaf.chickenstock.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record StockAnalysis(
        String ticker,

        @JsonProperty("stock")
        String name,

        @JsonProperty("anlysis_date")
        String analysisDate,

        String signal,
        String label,

        @JsonProperty("totoal_score")
        double totalScore,

        double confidence,

        @JsonProperty("axis_scores")
        Map<String, Double> axisScores,

        @JsonIgnore
        @JsonProperty("insider_trades")
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