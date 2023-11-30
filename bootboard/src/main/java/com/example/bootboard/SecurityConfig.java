package com.example.bootboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. 모든 인증되지 않은 요청을 허가
                // authorizeHttpRequests: 스프링 시큐리티에서 HTTP 규칙을 정의 메서드. 특정 URL 패턴의 접근 제어에 사용
                // AntPathRequestMatcher("/**")).permitAll(): /로 시작하는 URL (모든 URL) 에 대한 접근 허가
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())

                // 2. 스프링 시큐리티가 CSRF 처리시 H2 콘솔은 예외로 처리
                // 스프링 시큐리티 환경에선 H2 콘솔 로그인시 CSRF 로 인한 403 예외 발생
                // ignoringRequestMatchers: 특정 요청을 무시하도록 지시하는 메서드
                // new AntPathRequestMatcher("/h2-console/**") : h2 콘솔 접속시 필터체인 거치지 않도록
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))

                // 3. URL 요청시 X-Frame-Options 헤더값을 sameorigin으로 설정
                // 스프링 시큐리티는 사이트 콘텐츠가 다른 사이트에 포함되지 않기 위해 X-Frame-Options 사용
                // frame에 포함된 페이지가 제공하는 사이트와 동일한 경우 사용 가능하도록 설정
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                // 4. formLogin 메서드: 로그인 설정 담당.
                // loginPage(String loginFormUrl): 로그인 폼 URL
                // defaultSuccessUrl(loginSuccessUrl): 로그인 성공 후 이동할 URL
                // 로그인 URL: "/user/login", 로그인 성공시: "/" 로 이동
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))

                // 5. logout 메서드: 로그아웃 설정 담당
                // logoutSuccessUrl(String logoutSuccessUrl): 로그아웃 성공 후 이동할 URL
                // invalidateHttpSession(boolean invalidateHttpSession): HTTP 세션 무효화 결정
                // deleteCookies(String cookieNames): 삭제할 쿠키 이름 지정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    // 회원가입 메서드에서 사용할 암호화 방식: PasswordEncoder 객체 빈 주입
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 객체 빈 주입
    // AuthenticationManager 는 스프링 시큐리티의 인증을 담당
    // 사용자 인증시 UserSecurityService와 PasswordEncoder 사용
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
