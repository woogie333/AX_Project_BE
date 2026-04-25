package com.knuaf.chickenstock.stock.scheduler;

import com.knuaf.chickenstock.stock.dto.StockPrice;  // dto 패키지로 통일
import com.knuaf.chickenstock.stock.service.StockService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockScheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockService stockService;

    // @RequiredArgsConstructor 대신 생성자 직접 작성
    public StockScheduler(SimpMessagingTemplate messagingTemplate, StockService stockService) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
    }

    @Scheduled(fixedDelay = 3000)
    public void pushStockPrices() {
        StockPrice price = stockService.getLatestPrice("005930");
        messagingTemplate.convertAndSend("/topic/stock/005930", price);
    }
}