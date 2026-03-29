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