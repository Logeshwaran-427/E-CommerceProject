package com.project.NOTIFICATION.Entity;

import java.time.LocalDateTime;

import com.project.NOTIFICATION.Enums.NotificationChannel;
import com.project.NOTIFICATION.Enums.NotificationStatus;
import com.project.NOTIFICATION.Enums.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    String title;
    String message;

    @Enumerated(EnumType.STRING)
    NotificationType type;
    @Enumerated(EnumType.STRING)
    NotificationChannel channel;
    @Enumerated(EnumType.STRING)
    NotificationStatus status;
    LocalDateTime createdAt;

    public Notifications() {
    }

    public Notifications(Long userId, String title, String message, NotificationType type, NotificationChannel channel,
            NotificationStatus status, LocalDateTime createdAt) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.channel = channel;
        this.status = status;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void addCreatedAt(){
        this.createdAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
}
