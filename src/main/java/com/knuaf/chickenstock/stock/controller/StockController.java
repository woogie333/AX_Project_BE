package com.knuaf.chickenstock.stock.controller;

import com.knuaf.chickenstock.stock.service.StockService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @MessageMapping("/subscribe")
    @SendTo("/topic/ack")
    public String handleSubscribe(@Payload Map<String, String> payload) {
        String symbol = payload.get("symbol");
        return symbol + " 구독 시작";
    }
}