package com.knuaf.chickenstock.analysis.service;

import com.knuaf.chickenstock.analysis.dto.StockAnalysis;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    private final RestTemplate restTemplate;

    public AnalysisService(){
        this.restTemplate = new RestTemplate();
    }

    public StockAnalysis generateAnalysis(String symbol) {

        String aiServerUrl = "https://lfkgtsvw7qbcljiqbokr7xqs3q0nrgsw.lambda-url.ap-northeast-2.on.aws/";

        try{
            StockAnalysis aiResponse = restTemplate.getForObject(aiServerUrl, StockAnalysis.class);

            if(aiResponse==null){
                throw new RuntimeException("AI 서버로부터 응답을 받지 못했습니다.");
            }

            return new StockAnalysis(
                    symbol,
                    aiResponse.name(),
                    aiResponse.analysisDate(),
                    aiResponse.signal(),
                    aiResponse.label(),
                    aiResponse.totalScore(),
                    aiResponse.confidence(),
                    aiResponse.axisScores(),
                    aiResponse.insiderTrades()
            );
        }catch(Exception e){
            System.err.println("AI 리포트 통신 에러: " + e.getMessage());
            throw new RuntimeException("AI 분석 데이터를 불러오는데 실패했습니다.");
        }
    }
}