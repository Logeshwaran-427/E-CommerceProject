package com.project.INVENTORY.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.INVENTORY.Filter.JwtFiltervalidation;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    final JwtFiltervalidation jwtFilterValidation;

    SecurityConfig(JwtFiltervalidation jwtFilterValidation) {
        this.jwtFilterValidation = jwtFilterValidation;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(csrf->csrf.disable()).headers(frameOptions->frameOptions.disable()).
        authorizeHttpRequests(req->req.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated()).
        addFilterBefore(jwtFilterValidation, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
