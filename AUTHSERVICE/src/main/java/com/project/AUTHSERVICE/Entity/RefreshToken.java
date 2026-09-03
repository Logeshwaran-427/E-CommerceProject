package com.project.AUTHSERVICE.Entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String refreshToken;
    Date expiryDate;
    boolean revoked;

    @OneToOne
    UserDet userDet;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String refreshToken, Date expiryDate, boolean revoked, UserDet userDet) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
        this.userDet = userDet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public UserDet getUserDet() {
        return userDet;
    }

    public void setUserDet(UserDet userDet) {
        this.userDet = userDet;
    }

    
    
}
