package com.example.clothing4413.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends Users {
    protected Customer() {}

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }
}

/**
 * Currently a template, we will add features that seperate customers from administrators
 * in the future.
 * 
 * I.e shopping cart, order history, etc...
 */

