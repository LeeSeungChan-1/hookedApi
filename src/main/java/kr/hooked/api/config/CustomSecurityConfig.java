package kr.hooked.api.config;

import kr.hooked.api.filter.JWTCheckFilter;
import kr.hooked.api.handler.APILoginFailHandler;
import kr.hooked.api.handler.APILoginSuccessHandler;
import kr.hooked.api.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 비활성화
        http.csrf(csrf -> csrf.disable());

        // 세션 사용 안 함
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 인증 및 권한 정책
        http.authorizeHttpRequests(auth -> auth
                // Preflight 요청 허용
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 인증 없이 접근 허용할 엔드포인트
                .requestMatchers(
                        "/api/employee/login",
                        "/api/employee/signup",
                        "/api/token/refresh",
                        "/actuator/health",
                        "/swagger-ui/**", // Swagger UI 정적 리소스(HTML, JS, CSS 등)가 매핑되는 경로
                        "/v3/api-docs/**" // OpenAPI 스펙(JSON 형식)을 노출하는 엔드포인트
                ).permitAll()
                // 나머지 요청은 인증 필요
                .anyRequest().authenticated()
        );

        // 폼 기반 로그인 설정
        http.formLogin(form -> form
                .loginProcessingUrl("/api/employee/login")
                .successHandler(new APILoginSuccessHandler())
                .failureHandler(new APILoginFailHandler())
                .permitAll()
        );

        // JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 등록
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // 권한 예외 핸들러
        http.exceptionHandling(ex ->
                ex.accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList(
                "https://hooked.kr",
                "https://www.hooked.kr",
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
