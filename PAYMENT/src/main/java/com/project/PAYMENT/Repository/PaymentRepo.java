package com.project.PAYMENT.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.PAYMENT.Entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);
}
