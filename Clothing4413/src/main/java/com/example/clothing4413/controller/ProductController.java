package com.example.clothing4413.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.model.Product;
import com.example.clothing4413.repository.ProductRepository;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    @Autowired
    private ProductRepository repo;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        //testing purposes
        System.out.println("POST hit controller with: " + product.getName());
        return repo.save(product);
    }

    @GetMapping
    public List<Product> getProducts() {
        //testing purposes
        System.out.println("GET hit controller");
        return repo.findAll();
    }
}
