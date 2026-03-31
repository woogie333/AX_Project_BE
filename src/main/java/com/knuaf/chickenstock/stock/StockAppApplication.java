package com.knuaf.chickenstock.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockAppApplication.class, args);
    }
}