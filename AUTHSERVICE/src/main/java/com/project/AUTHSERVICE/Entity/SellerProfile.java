package com.project.AUTHSERVICE.Entity;

import com.project.AUTHSERVICE.Enums.SellerStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class SellerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank (message = "Business name is required")
    String businessName;
    @NotBlank (message = "Business email is required")
    @Email (message = "Enter valid email")
    String businessEmail;
    @NotBlank (message = "GST no is required")
    String gst;

    @Enumerated (EnumType.STRING)
    SellerStatus status;

    @OneToOne
    @JoinColumn(name = "userdet_id")
    UserDet userDet;



    public SellerProfile(String businessName, String businessEmail, String gst, SellerStatus status,UserDet userDet) {
        this.businessName = businessName;
        this.businessEmail = businessEmail;
        this.gst = gst;
        this.status = status;
        this.userDet=userDet;
    }

    public SellerProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDet getUserDet() {
        return userDet;
    }

    public void setUserDet(UserDet userDet) {
        this.userDet = userDet;
    }

    
}
