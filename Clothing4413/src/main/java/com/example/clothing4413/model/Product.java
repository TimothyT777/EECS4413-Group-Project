package com.example.clothing4413.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private Long product_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "image")
    private String image;

    /**
     * Probably some other product features we need to add
     * Maybe make a class with enum for product categories and add that as a field here
     * would help with the filtering feature.
     * 
     * idk what else stock maybe?
     * Just add as you go and as you need.
     */

    public Product() {}

    public Product(String name, String description, double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;

    }
    

    public Long getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImage(){
        return image;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image){
        this.image = image;
    }

}