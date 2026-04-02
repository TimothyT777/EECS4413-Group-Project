package com.example.clothing4413.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clothing4413.model.Product;
import com.example.clothing4413.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> findByName(String name) {
        return productRepo.findByName(name);
    }

    public Product findProductById(Long id) {
        return productRepo.findProductById(id);
    }

    public Product addProduct(String name, String description, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Product product = new Product(name, description, price, quantity);
        return productRepo.save(product);
    }

    public Product updateQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        product.setStock(quantity);
        return productRepo.save(product);
    }

    public void clear() {
        productRepo.deleteAll();
    }

    public void removeProductById(Long id) {
        productRepo.deleteById(id);
    }

    public boolean productExists(Long id) {
        return productRepo.existsById(id);
    }
}