package com.example.clothing4413.dto;

import com.example.clothing4413.model.ProductCategory;

/**
 * The product information of each product nested inside CartItem
 */
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private ProductCategory category;
    private double price;
    private int stock;
    private String image;

    public ProductResponse() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getBrand() { 
        return brand; 
    }

    public void setBrand(String brand) { 
        this.brand = brand; 
    }

    public ProductCategory getCategory() { 
        return category; 
    }

    public void setCategory(ProductCategory category) { 
        this.category = category; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public double getPrice() { 
        return price; 
    }

    public void setPrice(double price) { 
        this.price = price; 
    }

    public String getImage() { 
        return image; 
    }

    public void setImage(String image) { 
        this.image = image; 
    }

    public int getStock() { 
        return stock; 
    }

    public void setStock(int stock) { 
        this.stock = stock; 
    }
}