package com.project.ORDER.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ORDER.Entities.Address;

@Repository
public interface AddressRepo extends JpaRepository <Address,Long> {

    
    
}
