package com.iabacus.salespro.core.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iabacus.salespro.core.security.filter.CsrfCookieFilter;
import com.iabacus.salespro.core.security.filter.CustomAuthFilter;
import com.iabacus.salespro.core.security.filter.CustomAuthorizationFilter;
import com.iabacus.salespro.core.security.handler.*;
import com.iabacus.salespro.core.security.provider.CustomUserDetailsAuthenticationProvider;
import com.iabacus.salespro.web.login.repository.LoginHistoryRepository;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

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
    private final RoleRepository roleRepository;

    @Value("${base.url}")
    private String baseUrl;

    private final Environment env;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(baseUrl, "http://localhost:5173", "http://localhost:5174", "http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization", "X-CSRF-TOKEN"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(CORS_MAX_AGE);
                    return config;
                }))

                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/api/v1/auths/**", "/swagger-ui/**", "/api-docs/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomAuthorizationFilter(roleRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices()), UsernamePasswordAuthenticationFilter.class)

                .logout(config -> config
                        .logoutUrl(LOGOUT_URI)
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                        .deleteCookies("SESSION", "remember-me")
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
    public CustomAuthFilter usernamePasswordAuthenticationFilter() {
        CustomAuthFilter filter = new CustomAuthFilter(LOGIN_URI, objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new CustomLoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new DelegatingSecurityContextRepository(new HttpSessionSecurityContextRepository()));
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setRememberMeParameterName("remember");
        rememberMeServices.setValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY_SECONDS);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        ProviderManager providerManager = new ProviderManager(new CustomUserDetailsAuthenticationProvider(
                userDetailsService,
                passwordEncoder(),
                memberRepository,
                loginHistoryRepository
        ));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}
