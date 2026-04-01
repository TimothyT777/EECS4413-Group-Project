package com.example.clothing4413.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO form for the user to fill out their billing information when checking out a cart
 */
public class CheckoutRequest {
    private Long customerId;

    @NotBlank(message = "Shipping address is required") //Wont specify a pattern for this project
    private String shippingAddress;

    @NotBlank(message = "Billing address is required") //Wont specify a pattern for this project
    private String billingAddress;

    @NotBlank(message = "Cardholder name is required") //Wont specidy a pattern for this project
    private String cardHolderName;

    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Expiry date must be MM/YY format")
    @NotBlank(message = "Card expiry date is required")
    private String cardExpiry;

    boolean saveInfo; //If the customer wants to save their billing info

    public CheckoutRequest() {} //For JPA

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

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

    public boolean isSaveInfo() {
        return saveInfo;
    }

    public void setSaveInfo(boolean saveInfo) {
        this.saveInfo = saveInfo;
    }
}