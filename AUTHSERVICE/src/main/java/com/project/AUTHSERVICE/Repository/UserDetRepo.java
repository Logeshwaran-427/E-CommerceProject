package com.project.AUTHSERVICE.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.AUTHSERVICE.Entity.UserDet;

public interface UserDetRepo extends JpaRepository<UserDet,Long>{

    Optional<UserDet> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    
}
