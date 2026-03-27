package com.example.clothing4413.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


@Entity
@Table(name = "CART")
public class Cart {
    //Id of Cart
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //User can only have one cart.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Customer customer;

    //List of Products inside the cart.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    //When was the cart created.
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //When was the cart last updated.
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //Dont need when the cart was checked out, do that with Order

    //Before cart is placed in table, set the current times
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    //Before the cart is updated, set the updated time to current time.
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public Customer getUser() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void addProduct(Product product, int quantity) {
        //Check if the product is already in the cart. If it is, just update the quantity and return.
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())) {
                item.setQuantity(item.getQuantity() + quantity);
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }

        //If the product is not in the cart, add it.
        items.add(new CartItem(this, product, quantity));
        this.updatedAt = LocalDateTime.now();
    }  

    public void removeProduct(Product product) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())) {
                iterator.remove();
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }
    }

    //Manual replacement of quantity. Sets to specified quantity instead of adding to it.
    public void updateProductQuantity(Product product, int quantity) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())) {
                item.setQuantity(quantity);
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }
    }
}