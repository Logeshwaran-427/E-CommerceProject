package com.project.ORDER.ResponseDTO;

public class SellerDashboardDTO {

    Long totalOrders;
    Long paymentPending;
    Long confirmed;
    Long shipped;
    Long delivered;
    Long cancelled;

    public SellerDashboardDTO(Long totalOrders, Long paymentPending, Long confirmed, Long shipped, Long delivered,
            Long cancelled) {
        this.totalOrders = totalOrders;
        this.paymentPending = paymentPending;
        this.confirmed = confirmed;
        this.shipped = shipped;
        this.delivered = delivered;
        this.cancelled = cancelled;
    }

    public SellerDashboardDTO() {
    }
    
    public Long getTotalOrders() {
        return totalOrders;
    }
    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
    public Long getPaymentPending() {
        return paymentPending;
    }
    public void setPaymentPending(Long paymentPending) {
        this.paymentPending = paymentPending;
    }
    public Long getConfirmed() {
        return confirmed;
    }
    public void setConfirmed(Long confirmed) {
        this.confirmed = confirmed;
    }
    public Long getShipped() {
        return shipped;
    }
    public void setShipped(Long shipped) {
        this.shipped = shipped;
    }
    public Long getDelivered() {
        return delivered;
    }
    public void setDelivered(Long delivered) {
        this.delivered = delivered;
    }
    public Long getCancelled() {
        return cancelled;
    }
    public void setCancelled(Long cancelled) {
        this.cancelled = cancelled;
    }
    
}
