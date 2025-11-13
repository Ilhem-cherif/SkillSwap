package com.skillswap.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class userNameHeaderFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Mono<Authentication> auth2 = ReactiveSecurityContextHolder
                .getContext().map(e -> e.getAuthentication());

        return auth2.map(authentication -> {
                    String username = authentication.getName();
                    return exchange.getRequest().mutate()
                            .header("X-Authenticated-User", username)
                            .build();
                })
                .map(request -> exchange.mutate().request(request).build())
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);

    }
}
