package com.project.NOTIFICATION.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.NOTIFICATION.DTO.NotificationResponses;
import com.project.NOTIFICATION.Entity.Notifications;
import com.project.NOTIFICATION.Services.NotificationService;




@RestController
@RequestMapping("notification")
public class NotificationController {

    final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService=notificationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public NotificationResponses getNotifications(Pageable pageable) {
        return notificationService.listNotification(pageable);
    }
    
    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @GetMapping("/my")
    public NotificationResponses getMethodName(Pageable pageable) {
        return notificationService.listMyNotifications(pageable);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")

    @GetMapping("/latest")
    public List<Notifications> getMethodName() {
        return notificationService.latestNotifications();
    }
    
    

    
}
