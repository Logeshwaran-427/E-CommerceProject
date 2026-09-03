package com.project.ApiGateway.UtilClass;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jcajce.BCFKSLoadStoreParameter.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

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

    
    
}
