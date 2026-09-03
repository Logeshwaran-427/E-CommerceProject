package com.project.PRODUCT.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.PRODUCT.Filters.JwtFilterValidation;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtFilterValidation jwtFilterValidation;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(csrf->csrf.disable()).headers(frameOptions->frameOptions.disable()).
        authorizeHttpRequests(req->req.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated()).
        addFilterBefore(jwtFilterValidation, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
}
