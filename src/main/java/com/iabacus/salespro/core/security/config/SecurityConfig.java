package com.iabacus.salespro.core.security.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.security.filter.CsrfCookieFilter;
import com.iabacus.salespro.core.security.filter.CustomAuthFilter;
import com.iabacus.salespro.core.security.handler.CustomAccessDeniedHandler;
import com.iabacus.salespro.core.security.handler.CustomBasicAuthenticationEntryPoint;
import com.iabacus.salespro.core.security.handler.CustomLoginFailHandler;
import com.iabacus.salespro.core.security.handler.CustomLoginSuccessHandler;
import com.iabacus.salespro.core.security.provider.CustomUserDetailsAuthenticationProvider;
import com.iabacus.salespro.web.login.repository.LoginHistoryRepository;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    public static final long CORS_MAX_AGE = 3600L;
    public static final int REMEMBER_ME_TOKEN_VALIDITY_SECONDS = 3600 * 24 * 30; // 30일
    public static final String LOGIN_URI = "/api/v1/auths/login";
    public static final String LOGOUT_URI = "/api/v1/auths/logout";

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${base.url}")
    private String baseUrl;

    private final Environment env;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web
                .ignoring()
                .requestMatchers("/favicon.ico", "/error")
                .requestMatchers(toH2Console());
        };
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(STATELESS))
            .cors(corsConfig -> corsConfig.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList(baseUrl));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setAllowCredentials(true);
                config.setMaxAge(CORS_MAX_AGE);
                return config;
            }))

            .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers("/api/v1/auths/**", "/swagger-ui/**", "/api-docs/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            // .csrf(AbstractHttpConfigurer::disable)

            .addFilterBefore(abstractAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)

            .logout(config -> config
                .logoutUrl(LOGOUT_URI)
                .deleteCookies("SESSION", "remember-me", "XSRF-TOKEN")
            )

            .exceptionHandling(e -> {
                e.accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper));
                e.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint(objectMapper));
            })

            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/v1/auths/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );

        setRequiresChannel(http);
        return http.build();
    }

    private void setRequiresChannel(HttpSecurity http) throws Exception {
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles[0].contains("prod")) {
            http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure()); // https only
        } else {
            http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // http only
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter() {
        CustomAuthFilter filter = new CustomAuthFilter(LOGIN_URI, objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new CustomLoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setRememberMeParameterName("remember");
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY_SECONDS);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(new CustomUserDetailsAuthenticationProvider(
            userDetailsService,
            passwordEncoder(),
            memberRepository,
            loginHistoryRepository
        ));
    }

}
