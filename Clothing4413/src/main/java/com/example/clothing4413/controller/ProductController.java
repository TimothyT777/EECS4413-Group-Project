package com.example.clothing4413.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clothing4413.dto.AddProductRequest;
import com.example.clothing4413.dto.UpdateInventoryRequest;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/admin/inventory")
    public ResponseEntity<Product> addProduct(@RequestBody AddProductRequest request) {
        Product product = productService.addProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PatchMapping("/admin/inventory/{id}/quantity")
    public ResponseEntity<Product> updateQuantity(
            @PathVariable Long id,
            @RequestBody UpdateInventoryRequest request) {

        Product product = productService.updateQuantity(id, request.getQuantity());
        return ResponseEntity.ok(product);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}