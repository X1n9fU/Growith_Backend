package dev.book.global.config.security;

import dev.book.global.config.security.handler.ExceptionHandlerFilter;
import dev.book.global.config.security.handler.OAuth2FailureHandler;
import dev.book.global.config.security.handler.OAuth2SuccessHandler;
import dev.book.global.config.security.jwt.JwtAuthenticationFilter;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(
                                        new AntPathRequestMatcher("/oauth2/**"),
                                        new AntPathRequestMatcher("/login/oauth2/**"),
                                        new AntPathRequestMatcher("/favicon.ico"),
                                        new AntPathRequestMatcher("/signup"),
                                        new AntPathRequestMatcher("/login")
                                ).permitAll()
                                .anyRequest().permitAll()
                        //todo 현재는 모든 경로를 열어놓음 추후 endpoint마다 권한 설정

                )
                .oauth2Login(
                        oauth2 -> oauth2
                                .authorizationEndpoint(authorization ->
                                        authorization.authorizationRequestRepository(OAuth2AuthorizationRequestBasedOnCookieRepository()))
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(oAuth2FailureHandler)
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService))
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository OAuth2AuthorizationRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
}
