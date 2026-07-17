package com.healthcare.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimitKeyResolverConfig {

    @Bean
    public KeyResolver userTokenKeyResolver() {
        return exchange -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(Authentication::isAuthenticated)
                .cast(JwtAuthenticationToken.class)
                .map(jwtAuthenticationToken -> "user:" + jwtAuthenticationToken.getToken().getSubject())
                .switchIfEmpty(Mono.just("ip:" + exchange.getRequest().getRemoteAddress()));
    }
}
