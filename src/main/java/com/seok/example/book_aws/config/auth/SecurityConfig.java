package com.seok.example.book_aws.config.auth;

import com.seok.example.book_aws.entity.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
        http
                .csrf( httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable() )
                .headers( httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions( frameOptionsConfig -> frameOptionsConfig.disable() )
                )
                .authorizeHttpRequests( authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers( "/", "/css/**", "/images/**", "/js/**", "/h2-console/**" ).permitAll()
                        .requestMatchers( "/api/v1/**" ).hasRole( Role.USER.name() )
                        .anyRequest().authenticated()
                )
                .logout( logout -> logout.logoutSuccessUrl( "/" ) )
                .oauth2Login( httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .userInfoEndpoint( userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService( customOAuth2UserService )
                        )
                );
        return http.build();
    }
}
