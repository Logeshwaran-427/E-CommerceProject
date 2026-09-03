package com.project.AUTHSERVICE.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.AUTHSERVICE.Filters.JwtValidation;
import com.project.AUTHSERVICE.Service.UserDetService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    final UserDetService userDetService;
    final JwtValidation jwtValidation;

    SecurityConfig(JwtValidation jwtValidation, UserDetService userDetService) {
        this.jwtValidation = jwtValidation;
        this.userDetService = userDetService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable()).headers(frame->frame.disable()).
        authorizeHttpRequests(req->req.requestMatchers("/auth/login","/auth/createUser","/h2-console/**").permitAll().anyRequest().authenticated()).
        addFilterBefore(jwtValidation, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserDetService userDetService){
        return userDetService ;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder,UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }
    
}
