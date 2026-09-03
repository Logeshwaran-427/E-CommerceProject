package com.project.AUTHSERVICE.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AUTHSERVICE.Entity.SellerProfile;
import com.project.AUTHSERVICE.Entity.UserDet;

@Repository
public interface SellerRepo extends JpaRepository<SellerProfile,Long> {

    SellerProfile findByUserDet(UserDet userDet);

    
    
}
