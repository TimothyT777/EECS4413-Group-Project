package com.example.clothing4413.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * A class that stores every order a user has made
 */
@Entity
@Table(name = "ORDERS")
public class Order {
    //Order id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //The customer the order belongs to
    //Many orders can belong to a single customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    //List of all items in the order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    //When the check out was completed (When the order was created)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Order() {} //For JPA

    public Order(Customer customer) {
        this.customer = customer;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}