package com.project.ApiGateway.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.project.ApiGateway.Filters.JwtValidationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    JwtValidationFilter jwtValidationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)throws Exception{
        http.csrf(csrf->csrf.disable()).
        authorizeExchange(req->req.pathMatchers("/auth/login","/auth/createUser").permitAll().
        anyExchange().authenticated()).addFilterBefore(jwtValidationFilter,SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    
    
}
