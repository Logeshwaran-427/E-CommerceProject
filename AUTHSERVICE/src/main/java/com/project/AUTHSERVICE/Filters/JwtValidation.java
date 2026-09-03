package com.project.AUTHSERVICE.Filters;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.AUTHSERVICE.Service.UserDetService;
import com.project.AUTHSERVICE.UtilClasses.JwtUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidation extends OncePerRequestFilter {

    final JwtUtility jwtUtility;

    final UserDetService userDetService;

       JwtValidation(JwtUtility jwtUtility, UserDetService userDetService) {
              this.jwtUtility = jwtUtility;
              this.userDetService = userDetService;
       }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String header=request.getHeader("Authorization");
                if(header!=null && header.startsWith("Bearer ")){
                    String token=header.substring(7);
                    System.out.println(token);
                    String name=jwtUtility.extractUserName(token);
                    System.out.println(name);
                    if(name !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                        System.out.println(name+" security");
                        UserDetails ud=userDetService.loadUserByUsername(name);
                        if(jwtUtility.validToken(token, ud)){
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(ud,null, ud.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
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
