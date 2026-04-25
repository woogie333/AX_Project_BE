package com.knuaf.chickenstock.analysis.service;

import com.knuaf.chickenstock.analysis.dto.StockAnalysis;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    /**
     * 주식 종목 코드를 받아 AI 분석 리포트 데이터를 생성합니다.
     * (추후 AI 담당 팀원이 이 내부 로직을 실제 AI 모델 연동 코드로 교체하면 됩니다.)
     */
    public StockAnalysis generateAnalysis(String symbol) {

        // 종목명 임시 매핑 (실제로는 DB나 외부 API에서 가져와야 함)
        String stockName = symbol.equals("005930") ? "삼성전자" : "알 수 없는 종목";

        // 현재 시간을 "yyyy-MM-dd HH:mm:ss" 형태로 포맷팅
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 프론트엔드 UI 테스트를 위한 더미 데이터 반환
        return new StockAnalysis(
                symbol,
                stockName,
                currentTime,
                "neutral",  // 신호 (strong_buy, buy, neutral, sell, strong_sell)
                "⚖️ 중립", // 화면에 보여질 텍스트
                1.03,       // 종합 점수 (-10 ~ 10)
                35.1,       // AI 신뢰도 (%)
                Map.of(
                        "technical", -2.0,
                        "fundamental", 0.4,
                        "financial", 7.9,
                        "macro", 1.38,
                        "sentiment", -2.71
                ),
                List.of(
                        new StockAnalysis.InsiderTrade("2026-03-31", "홍길동", "buy", 500, 30000000.0),
                        new StockAnalysis.InsiderTrade("2026-04-15", "김철수", "sell", 200, 15000000.0)
                )
        );
    }
}