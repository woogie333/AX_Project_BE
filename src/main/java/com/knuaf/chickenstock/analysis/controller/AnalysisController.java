package com.knuaf.chickenstock.analysis.controller;

import com.knuaf.chickenstock.analysis.dto.StockAnalysis;
import com.knuaf.chickenstock.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalysisController {
    private final AnalysisService analysisService;
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }
    // 리액트에서 버튼을 눌렀을 때 호출하는 경로: GET /api/analysis/005930
    @GetMapping("/{symbol}")
    public ResponseEntity<StockAnalysis> getStockAnalysis(@PathVariable String symbol) {
        // AI 담당 친구가 만든 로직을 여기서 호출하게 됩니다.
        StockAnalysis result = analysisService.generateAnalysis(symbol);
        return ResponseEntity.ok(result);
    }
}
