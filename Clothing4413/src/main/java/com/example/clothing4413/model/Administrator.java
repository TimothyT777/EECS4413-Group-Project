package com.example.clothing4413.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends Users {
    protected Administrator() {}

    public Administrator(String name, String email, String password) {
        super(name, email, password);
    }
}

/**
 * Currently a template, we will add features that seperate administator from customers
 * in the future.
 */