package com.example.clothing4413.dto;

/**
 * Sends information about an item in the cart to the frontend.
 */
public class CartItemResponse {
    private Long id;
    private ProductResponse product;
    private int quantity;

    public CartItemResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}