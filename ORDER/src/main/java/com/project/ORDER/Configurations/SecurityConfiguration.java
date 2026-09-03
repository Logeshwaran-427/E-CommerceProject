package com.project.ORDER.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.ORDER.Filters.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    final JwtFilter jwtFilter;
    

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){

        http.csrf(csrf->csrf.disable()).headers(frame->frame.disable()).
        authorizeHttpRequests(req->req.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated()).
        addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
}
