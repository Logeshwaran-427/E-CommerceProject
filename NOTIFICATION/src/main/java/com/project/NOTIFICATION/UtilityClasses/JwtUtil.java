package com.project.NOTIFICATION.UtilityClasses;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {


    final String SECRETKEY="Thisismysecretkeywhichholdstheenoughbitstoconvert";
    Key key=Keys.hmacShaKeyFor(SECRETKEY.getBytes());


    public Claims extraxtBody(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token){
        return extraxtBody(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extraxtBody(token).getExpiration().before(new Date());
    }

    public String getRole(String token){
        return extraxtBody(token).get("role", String.class);
    }

    public Long userId(String token){
        return extraxtBody(token).get("userId",Long.class);
    }

    
    
}
