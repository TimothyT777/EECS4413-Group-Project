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

