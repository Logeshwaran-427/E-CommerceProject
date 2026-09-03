package com.project.PRODUCT.Filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.PRODUCT.ExceptionHandling.AuthorizationException;
import com.project.PRODUCT.UtilityClasses.JwtUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterValidation extends OncePerRequestFilter {

    @Autowired
    JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                String path = request.getRequestURI();

                if(path.startsWith("/h2-console")|| path.startsWith("/favicon.ico")) {
                    filterChain.doFilter(request, response);
                    return;
                }
        
                String header=request.getHeader("Authorization");
                if(header==null){
                    throw new AuthorizationException("Authorization header is missing");
                }
                if(!header.startsWith("Bearer ")){
                    throw new AuthorizationException("Your Bearer token is invalid");
                }

            try{

                String token=header.substring(7);
                String name=jwtUtility.extractUserName(token);
                if(name!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    
                    if(!jwtUtility.isValidToken(token)){
                        
                        String role=jwtUtility.extractRole(token);
                        List<SimpleGrantedAuthority> authority=new ArrayList<>();
                        authority.add(new SimpleGrantedAuthority("ROLE_"+role));
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken( name,null,authority);
                        Long sellerId=jwtUtility.extractSellerId(token);
                        usernamePasswordAuthenticationToken.setDetails(sellerId);
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        
                    }
                }
            }
            catch(AuthorizationException ex){
                throw new AuthorizationException("Invalid token or credentials");

            }
            
            String correlationId = request.getHeader("X-Correlation-ID");

                if (correlationId != null) {
                MDC.put("correlationId", correlationId);
                }

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    MDC.clear();
                }
    }

    
}
