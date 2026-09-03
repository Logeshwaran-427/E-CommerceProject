package com.project.ApiGateway.Filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@Component
@Order(1)
public class LoggingFilter implements GlobalFilter,Ordered {

    // @Override
    // public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    //     Logger log=LoggerFactory.getLogger(LoggingFilter.class);
    //     String corrId=exchange.getAttribute("correlationId");

    //     log.info("[{}] Incoming request - " + exchange.getRequest().getMethod()+exchange.getRequest().getPath(),corrId);
    //     return chain.filter(exchange).doFinally(signalType->log.info("[{}] Response status - "+exchange.getResponse().getStatusCode(),corrId));
    // }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Logger log=LoggerFactory.getLogger(LoggingFilter.class);
        String corrId=exchange.getAttribute("correlationId");

        log.info("[{}] Incoming request - " + exchange.getRequest().getMethod()+exchange.getRequest().getPath(),corrId);
        return chain.filter(exchange).doFinally(signalType->log.info("[{}] Response status - "+exchange.getResponse().getStatusCode(),corrId));
  
    }

    @Override
    public int getOrder() {
        return -99;
    }
    
}
