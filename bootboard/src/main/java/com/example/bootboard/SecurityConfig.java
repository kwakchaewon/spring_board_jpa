package com.example.bootboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration

public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                // 1. 모든 인증되지 않은 요청을 허락
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())

                // 2. 스프링 시큐리티가 CSRF 처리시 H2 콘솔은 예외로 처리
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))

                // 3. 사이트의 콘텐츠가 다른 사이트에 포함되지 않도록 하기 위해 X-Frame-Options 헤더값을 사용해 방지
                // X-Frame-Options 헤더값을 sameorigin으로 설정 -> frame에 포함된 페이지가 페이지를 제공하는 사이트와 동일한 경우 사용 가능
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
        ;
        return http.build();
    }

}
