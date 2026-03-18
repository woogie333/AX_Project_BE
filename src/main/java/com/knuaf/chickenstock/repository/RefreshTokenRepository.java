package com.knuaf.chickenstock.repository;

import com.knuaf.chickenstock.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByLoginId(String loginId);
    Optional<RefreshToken> findByRefreshToken(String token);
}
