package com.project.NOTIFICATION.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.NOTIFICATION.Entity.Notifications;


@Repository
public interface NotificationRepository extends JpaRepository<Notifications,Long> {
    
    Page<Notifications> findAll(Pageable pageable);

    Page<Notifications> findAllByUserId(Long userId,Pageable pageable);

    List<Notifications> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);

}
