package com.project.ApiGateway.Filters;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.project.ApiGateway.ExceptionHandling.UnAuthorizedException;
import com.project.ApiGateway.UtilClass.JwtUtil;

import reactor.core.publisher.Mono;

@Component
@Order(2)
public class JwtValidationFilter implements WebFilter {
    
    @Autowired
    JwtUtil jwtUtil;

    private static final Logger log=LoggerFactory.getLogger(JwtValidationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path=exchange.getRequest().getURI().getPath();
        if(path.contains("/auth/login")|| path.contains("/auth/createUser") ){
            return chain.filter(exchange);
        }
        
        String header=exchange.getRequest().getHeaders().getFirst("Authorization");
        if(header==null){
            throw new UnAuthorizedException("Authorization header is missing");
        }
        if(!header.startsWith("Bearer ")){
            return Mono.error(new UnAuthorizedException("Bearer token is missing"));
        }

        try{
        String token=header.substring(7);
        if(jwtUtil.isValidToken(token)){
            throw new UnAuthorizedException("Your token is expired");
        }

        
        String name=jwtUtil.extractUserName(token);
        String role=jwtUtil.extractRole(token);
        List<SimpleGrantedAuthority> authority=new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(name,null,authority);
        log.info("Validation completed for {} {}",name,role);
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(usernamePasswordAuthenticationToken));
        }
        catch(Exception ex){
            return Mono.error(new UnAuthorizedException("Token invalid"));
        }
    }

    
}
