package com.project.ORDER.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ORDER.Entities.Order1;

@Repository
public interface OrderRepo extends JpaRepository<Order1,Long> {

    Optional<Order1> findByUserId(Long userId);

    List<Order1> findAllByUserId(Long userId);

    
}
