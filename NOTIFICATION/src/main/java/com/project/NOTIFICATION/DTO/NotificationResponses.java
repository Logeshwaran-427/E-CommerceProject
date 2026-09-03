package com.project.NOTIFICATION.DTO;

import java.util.List;


import com.project.NOTIFICATION.Entity.Notifications;

public class NotificationResponses {
    
    List<Notifications> notifications;
    int currentPage;
    int totalPages;

    public NotificationResponses(List<Notifications> notifications, int currentPage, int totalPages) {
        this.notifications = notifications;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public NotificationResponses() {
    }
    
    public List<Notifications> getNotifications() {
        return notifications;
    }
    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
