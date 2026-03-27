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

    /**
     * For the person doing the product catalogue filtering, you are going 
     * to want to do it here and write a function to return a list of products.
     * that match the filter criteria from either a search query or something else.
     */
}