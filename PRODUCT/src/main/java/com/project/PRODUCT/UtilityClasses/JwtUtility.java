package com.project.PRODUCT.UtilityClasses;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {

    final String SECRETKEY="Thisismysecretkeywhichholdstheenoughbitstoconvert";
    Key key=Keys.hmacShaKeyFor(SECRETKEY.getBytes());


    public Claims extractBody(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String extractUserName(String token){
        return extractBody(token).getSubject();
    }

    public boolean isValidToken(String token){
        return extractBody(token).getExpiration().before(new Date());
    }

    public String extractRole(String token){
        return extractBody(token).get("role",String.class);
    }

    public Long extractSellerId(String token){
        return extractBody(token).get("seller_id",Long.class);
    }

    
}
