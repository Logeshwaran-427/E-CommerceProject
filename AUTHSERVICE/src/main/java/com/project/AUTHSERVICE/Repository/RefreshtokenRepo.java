package com.project.AUTHSERVICE.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AUTHSERVICE.Entity.RefreshToken;
import com.project.AUTHSERVICE.Entity.UserDet;

@Repository
public interface RefreshtokenRepo extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByUserDet(UserDet userDet);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    
}
