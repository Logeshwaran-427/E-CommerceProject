package com.project.ApiGateway.Filters;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId=exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");

        if(correlationId==null){
            correlationId=UUID.randomUUID().toString();   
        }
        exchange.getAttributes().put("correlationId", correlationId);
        ServerWebExchange modifiedExchange=exchange.mutate().request(exchange.getRequest().mutate().header("X-Correlation-ID", correlationId).build()).build();
        return chain.filter(modifiedExchange).doFinally(signal->MDC.clear());
    }

    @Override
    public int getOrder() {
        return -100;
    }


    
}
