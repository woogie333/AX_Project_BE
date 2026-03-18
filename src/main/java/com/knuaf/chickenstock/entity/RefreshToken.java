package com.knuaf.chickenstock.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String loginId;

    public RefreshToken(String token, String studentId) {
        this.refreshToken = token;
        this.loginId = loginId;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }


}