package com.example.clothing4413.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Sends the full cart information to the frontend
 */
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;

    public CartResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    @JsonProperty("total")
    public double getTotalPrice() {
        if (items == null) {
            return 0.0;
        }

        double total = 0.0;
        for (CartItemResponse item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return Math.round(total * 100.0) / 100.0; // Round to 2 decimal places
    }

    @JsonProperty("itemCount")
    public int getItemCount() {
        if (items == null) {
            return 0;
        }

        int count = 0;
        for (CartItemResponse item : items) {
            count += item.getQuantity();
        }
        return count;
    }
}
    
