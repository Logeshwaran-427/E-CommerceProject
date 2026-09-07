package com.project.AUTHSERVICE.DTO;

import com.project.AUTHSERVICE.Enums.SellerStatus;


public class SellerDetailsResponse {
    Long sellerId;
    String businessName;
    String businessEmail;
    String gst;
    SellerStatus status;
    Long userid;
    String username;

    public SellerDetailsResponse() {
    }

    public SellerDetailsResponse(String businessName, String businessEmail, String gst, SellerStatus status,
            Long userid, String username, Long sellerId) {
        this.businessName = businessName;
        this.businessEmail = businessEmail;
        this.gst = gst;
        this.status = status;
        this.userid = userid;
        this.username = username;
    }
        public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getBusinessEmail() {
        return businessEmail;
    }
    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }
    public String getGst() {
        return gst;
    }
    public void setGst(String gst) {
        this.gst = gst;
    }
    public SellerStatus getStatus() {
        return status;
    }
    public void setStatus(SellerStatus status) {
        this.status = status;
    }
    public Long getUserid() {
        return userid;
    }
    public void setUserid(Long userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
