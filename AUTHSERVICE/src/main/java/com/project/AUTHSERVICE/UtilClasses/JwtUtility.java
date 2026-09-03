package com.project.AUTHSERVICE.UtilClasses;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.project.AUTHSERVICE.Entity.SellerProfile;
import com.project.AUTHSERVICE.Entity.UserDet;
import com.project.AUTHSERVICE.Enums.RoleEnum;
import com.project.AUTHSERVICE.Repository.SellerRepo;
import com.project.AUTHSERVICE.Repository.UserDetRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {

    final UserDetRepo userDetRepo;

    final SellerRepo sellerRepo;

    final String SECRETKEY="Thisismysecretkeywhichholdstheenoughbitstoconvert";
    Key key=Keys.hmacShaKeyFor(SECRETKEY.getBytes());
    long expiryTime=1000*60*60;

    long refreshExpiryTime=1000*60*180;

    JwtUtility(UserDetRepo userDetRepo, SellerRepo sellerRepo) {
        this.userDetRepo = userDetRepo;
        this.sellerRepo = sellerRepo;
    }

    public String generateToken(String name,String role){

        System.out.println(role);
        System.out.println(name);

        Map<String,String> roleClaims=new HashMap<>();
        roleClaims.put("role", role);

        UserDet userDet=userDetRepo.findByUsername(name).orElseThrow();
        Long sellerId=null;
        if(userDet.getRole()==RoleEnum.PRODUCT_OWNER){
            SellerProfile seller=sellerRepo.findByUserDet(userDet);
            sellerId=seller.getId();
        }

        return Jwts.builder().
                        setClaims(roleClaims).
                        claim("user_id", userDet.getId()).
                        claim("seller_id", sellerId).setSubject(name).
                        setIssuedAt(new Date()).
                        setExpiration(new Date(System.currentTimeMillis()+expiryTime)).
                        signWith(key,SignatureAlgorithm.HS256).compact();

    }


    public String generateRefreshToken(String name,String role){

        System.out.println(role);
        System.out.println(name);

        Map<String,String> roleClaims=new HashMap<>();
        roleClaims.put("role", role);

        UserDet userDet=userDetRepo.findByUsername(name).orElseThrow();
        Long sellerId=null;
        if(userDet.getRole()==RoleEnum.PRODUCT_OWNER){
            SellerProfile seller=sellerRepo.findByUserDet(userDet);
            sellerId=seller.getId();
        }

        return Jwts.builder().
                        setClaims(roleClaims).
                        claim("user_id", userDet.getId()).
                        claim("seller_id", sellerId).setSubject(name).
                        setIssuedAt(new Date()).
                        setExpiration(new Date(System.currentTimeMillis()+refreshExpiryTime)).
                        signWith(key,SignatureAlgorithm.HS256).compact();

    }


    public Claims extractBody(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    }

    public String extractUserName(String token){
        return extractBody(token).getSubject();
    }

    public boolean checkExpiryTime(String token){
        return extractBody(token).getExpiration().before(new Date());
    }

    public boolean validToken(String token, UserDetails userDetails){
        return !checkExpiryTime(token) && userDetails.getUsername().equals(extractUserName(token));

    }
    
}
