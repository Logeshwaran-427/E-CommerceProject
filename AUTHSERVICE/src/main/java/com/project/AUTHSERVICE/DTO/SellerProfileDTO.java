package com.project.AUTHSERVICE.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SellerProfileDTO {

    @NotBlank (message = "Business name is required")
    String businessName;
    @NotBlank (message = "Business email is required")
    @Email (message = "Enter valid email")
    String businessEmail;
    @NotBlank (message = "GST no is required")
    String gst;
    
    public SellerProfileDTO(@NotBlank(message = "Business name is required") String businessName,
            @NotBlank(message = "Business email is required") @Email(message = "Enter valid email") String businessEmail,
            @NotBlank(message = "GST no is required") String gst) {
        this.businessName = businessName;
        this.businessEmail = businessEmail;
        this.gst = gst;
    }
    public SellerProfileDTO() {
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

    
    
}
