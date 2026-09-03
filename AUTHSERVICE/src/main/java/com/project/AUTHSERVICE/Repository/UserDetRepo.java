package com.project.AUTHSERVICE.Repository;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.AUTHSERVICE.Entity.UserDet;

@Repository
public interface UserDetRepo extends JpaRepository<UserDet,Long>{

    Optional<UserDet> findByUsername(String username);
    
}
