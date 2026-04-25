package com.knuaf.chickenstock.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuaf.chickenstock.stock.dto.StockPrice;
import jakarta.annotation.PostConstruct; // ★ 추가됨
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType; // ★ 추가됨
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kis.api.key}")
    private String apiKey;

    @Value("${kis.api.secret}")
    private String apiSecret;

    // ★ 발급받은 출입증(토큰)을 들고 있을 변수
    private String accessToken;

    public StockService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 1. 토큰 발급 로직
     * @PostConstruct: 이 클래스가 생성되고(서버가 켜지고) 나서 딱 한 번 자동으로 실행되는 어노테이션
     */
    @PostConstruct
    public void generateToken() {
        System.out.println("🔑 한국투자증권 API 토큰 발급 시작...");
        try {
            // 토큰을 받기 위해 보내야 하는 Body 데이터 세팅
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("appkey", apiKey);
            body.put("appsecret", apiSecret);

            // POST 요청으로 토큰 발급 API 찌르기
            String response = webClient.post()
                    .uri("/oauth2/tokenP")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 응답받은 JSON에서 access_token 값만 쏙 빼서 변수에 저장
            JsonNode rootNode = objectMapper.readTree(response);
            this.accessToken = rootNode.path("access_token").asText();

            System.out.println("토큰 발급 성공! (Bearer " + this.accessToken.substring(0, 10) + "... )");

        } catch (Exception e) {
            System.err.println("토큰 발급 실패: " + e.getMessage());
        }
    }

    /**
     * 2. 주식 데이터 조회 로직 (기존과 동일하지만 헤더에 토큰 추가)
     */
    public StockPrice getLatestPrice(String symbol) {
        try {
            // 토큰이 아직 발급 안 됐다면 에러 방지
            if (accessToken == null) {
                throw new RuntimeException("토큰이 아직 발급되지 않았습니다.");
            }

            String response = webClient.get()
                    .uri("/uapi/domestic-stock/v1/quotations/inquire-price?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=" + symbol)
                    // ★ 핵심: KIS 서버가 요구하는 Bearer 토큰 헤더 추가
                    .header("authorization", "Bearer " + accessToken)
                    .header("appkey", apiKey)
                    .header("appsecret", apiSecret)
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode outputNode = rootNode.path("output");

            long currentPrice = Long.parseLong(outputNode.path("stck_prpr").asText());
            long openPrice = Long.parseLong(outputNode.path("stck_oprc").asText());
            long highPrice = Long.parseLong(outputNode.path("stck_hgpr").asText());
            long lowPrice = Long.parseLong(outputNode.path("stck_lwpr").asText());

            return new StockPrice(
                    symbol,
                    openPrice,
                    highPrice,
                    lowPrice,
                    currentPrice,
                    Instant.now().getEpochSecond()
            );

        } catch (Exception e) {
            System.err.println("실시간 API 호출 에러: " + e.getMessage());
            return new StockPrice(symbol, 0L, 0L, 0L, 0L, Instant.now().getEpochSecond());
        }
    }
}