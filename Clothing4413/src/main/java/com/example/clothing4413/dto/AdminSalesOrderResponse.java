package com.example.clothing4413.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AdminSalesOrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDateTime createdAt;
    private List<AdminSalesItemResponse> items;

    public AdminSalesOrderResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<AdminSalesItemResponse> getItems() {
        return items;
    }

    public void setItems(List<AdminSalesItemResponse> items) {
        this.items = items;
    }

    public double getTotal() {
        if (items == null) {
            return 0;
        }

        double total = 0;
        for (AdminSalesItemResponse item : items) {
            total += item.getSubtotal();
        }

        return Math.round(total * 100.0) / 100.0;
    }
}