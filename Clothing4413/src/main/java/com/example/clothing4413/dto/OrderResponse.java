package com.example.clothing4413.dto;

import java.time.LocalDateTime;
import  java.util.List;

public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;

    public OrderResponse() {}

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItemResponse> getItems() {
        return this.items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotal() {
        if (items == null) {
            return 0;
        }

        double total = 0;
        for (OrderItemResponse item : items) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100) / 100;
    }
}