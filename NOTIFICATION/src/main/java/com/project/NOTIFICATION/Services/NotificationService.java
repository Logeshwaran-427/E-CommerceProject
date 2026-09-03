package com.project.NOTIFICATION.Services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.NOTIFICATION.DTO.NotificationResponses;
import com.project.NOTIFICATION.DTO.OrderItemStatusNotification;
import com.project.NOTIFICATION.DTO.OrderStatusNotification;
import com.project.NOTIFICATION.DTO.UserContext;
import com.project.NOTIFICATION.Entity.Notifications;
import com.project.NOTIFICATION.Enums.NotificationChannel;
import com.project.NOTIFICATION.Enums.NotificationStatus;
import com.project.NOTIFICATION.Enums.NotificationType;
import com.project.NOTIFICATION.Repository.NotificationRepository;

@Service
public class NotificationService {

    final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }

    @KafkaListener(topics = {"order-confirmed","order-shipped","order-delivered"}, groupId = "notification-service")
    public void sendOrderNotification(OrderStatusNotification orderNotification){

        System.out.println("User "+ orderNotification.getUserId() + "received notification for "+ orderNotification.getType() );

        Notifications notifications=new Notifications();
        notifications.setUserId(orderNotification.getUserId());
        notifications.setMessage(orderNotification.getMessage()); 
        notifications.setType(orderNotification.getType());
        notifications.setChannel(NotificationChannel.APP_NOTIFICATION);
        notifications.setStatus(NotificationStatus.UNREAD);
        notifications.setTitle(orderNotification.getType().name());
        notificationRepository.save(notifications);
    }

    @KafkaListener(topics = {"orderItem-shipped","orderItem-delivered"}, groupId = "notification-service")
    public void sendOrderItemNotification(OrderItemStatusNotification orderItemStatusNotification){
        System.out.println("User "+ orderItemStatusNotification.getUserId() + "received notification for "+ orderItemStatusNotification.getType() );
        Notifications notifications=new Notifications();
        notifications.setUserId(orderItemStatusNotification.getUserId());
        notifications.setMessage(orderItemStatusNotification.getMessage());
        notifications.setChannel(NotificationChannel.APP_NOTIFICATION);
        notifications.setStatus(NotificationStatus.UNREAD);
        notifications.setType(orderItemStatusNotification.getType());
        notifications.setTitle(orderItemStatusNotification.getType().name());
        notificationRepository.save(notifications);
    }
    
    public NotificationResponses listNotification(Pageable pageable){

        Page<Notifications> allNotifications=notificationRepository.findAll(pageable);

        NotificationResponses notificationResponses=new NotificationResponses();
        notificationResponses.setNotifications(allNotifications.getContent());
        notificationResponses.setCurrentPage(allNotifications.getNumber());
        notificationResponses.setTotalPages(allNotifications.getTotalPages());


        return notificationResponses;

    }

    public NotificationResponses listMyNotifications(Pageable pageable){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        UserContext userContext= (UserContext) authentication.getDetails();
        Long userId=userContext.getUserId();

        Page<Notifications> userNotifications=notificationRepository.findAllByUserId(userId, pageable);

        NotificationResponses notificationResponses=new NotificationResponses();
        notificationResponses.setNotifications(userNotifications.getContent());
        notificationResponses.setCurrentPage(userNotifications.getNumber());
        notificationResponses.setTotalPages(userNotifications.getTotalPages());


        return notificationResponses;
    }

    public List<Notifications> latestNotifications(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        UserContext userContext= (UserContext) authentication.getDetails();
        Long userId=userContext.getUserId();

         List<Notifications> lastNotifications=notificationRepository.findTop20ByUserIdOrderByCreatedAtDesc(userId);

         return lastNotifications;
    }





}
