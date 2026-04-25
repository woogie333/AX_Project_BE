package com.knuaf.chickenstock.configuration;

import com.knuaf.chickenstock.jwt.JwtAuthenticationFilter;
import com.knuaf.chickenstock.jwt.JwtTokenProvider;
import com.knuaf.chickenstock.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/join", "/h2-console/**", "/error", "/ws-stock/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenRepository),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 프론트엔드 도메인 (예: 리액트 기본 포트 3000, 뷰 5173 등) -> 실제 베포할때는 도메인 주소 넣기
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));

        // 허용할 HTTP 메서드 (GET, POST, PUT, DELETE, OPTIONS 등) -> 그래도 오류 발생시 *List.of(*)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 프론트에서 보낼 수 있는 헤더 종류 허용
        configuration.setAllowedHeaders(List.of("*"));

        // 프론트엔드에서 인증 정보(쿠키, 토큰 등)를 포함해서 보낼 수 있게 허용
        configuration.setAllowCredentials(true);

        // 🚨 [매우 중요] 프론트엔드에서 우리가 만든 JWT 토큰 헤더를 읽을 수 있게 노출해줍니다!
        configuration.setExposedHeaders(List.of("Access_Token", "Refresh_Token"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 주소("/**")에 대해 위에서 설정한 CORS 규칙을 적용합니다.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}