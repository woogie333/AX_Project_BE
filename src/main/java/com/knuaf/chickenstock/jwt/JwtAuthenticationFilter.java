package com.knuaf.chickenstock.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuaf.chickenstock.dto.ResponseDto;
import com.knuaf.chickenstock.dto.TokenInfo;
import com.knuaf.chickenstock.entity.RefreshToken;
import com.knuaf.chickenstock.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String accessToken = jwtTokenProvider.getHeaderToken(httpRequest, "Access");
        String refreshToken = jwtTokenProvider.getHeaderToken(httpRequest, "Refresh");

        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                setAuthentication(accessToken);
            } else if (refreshToken != null) {
                if (jwtTokenProvider.refreshTokenValidation(refreshToken)) {
                    Optional<RefreshToken> savedToken = refreshTokenRepository.findByRefreshToken(refreshToken);
                    if (savedToken.isPresent()) {
                        String loginId = savedToken.get().getLoginId();
                        TokenInfo tokenInfo = jwtTokenProvider.generateToken(loginId, Collections.singletonList("ROLE_USER"));
                        jwtTokenProvider.setHeaderAccessToken(httpResponse, tokenInfo.getAccessToken());
                        setAuthentication(tokenInfo.getAccessToken());
                    }
                } else {
                    jwtExceptionHandler(httpResponse, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        try {
            ResponseDto responseDto = new ResponseDto(status.value(), msg, null);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}