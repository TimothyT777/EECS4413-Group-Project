package com.example.clothing4413.dto;

/**
 * Stored customer information when checking out a cart
 */
public class CustomerInfoResponse {
    private String shippingAddress;
    private String billingAddress;
    private String cardHolderName;
    private String cardNumber;
    private String cardExpiry;
    private boolean hasSavedInfo;

    public CustomerInfoResponse() {} //For JPA

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public boolean isHasSavedInfo() {
        return hasSavedInfo;
    }

    public void setHasSavedInfo(boolean hasSavedInfo) {
        this.hasSavedInfo = hasSavedInfo;
    }


}