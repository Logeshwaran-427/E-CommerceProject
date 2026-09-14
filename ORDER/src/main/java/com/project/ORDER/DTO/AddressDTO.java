package com.project.ORDER.DTO;

import com.project.ORDER.Enums.AddressType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddressDTO {
    @NotBlank (message = "This field is required")
    private String fullName;
    @NotBlank (message = "Phone number is required")
    private String phoneNumber;
    @NotBlank (message = "Address is required")
    private String address;
    @NotBlank (message = "City is required")
    private String city;
    @NotBlank (message = "Postal code is required")
    private String postalCode;
    @NotNull  (message = "Choose adresstype")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    public AddressDTO(@NotBlank(message = "This field is required") String fullName,
            @NotBlank(message = "Phone number is required") String phoneNumber,
            @NotBlank(message = "Address is required") String address,
            @NotBlank(message = "City is required") String city,
            @NotBlank(message = "Postal code is required") String postalCode,
            @NotBlank(message = "Choose adresstype") AddressType addressType) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.addressType = addressType;
    }
    public AddressDTO() {
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public AddressType getAddressType() {
        return addressType;
    }
    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }
}
