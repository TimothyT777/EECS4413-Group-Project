package com.example.clothing4413.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.clothing4413.dto.UpdateInventoryRequest;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.service.ImageStorageService;
import com.example.clothing4413.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ImageStorageService imageStorageService;

    public ProductController(ProductService productService, ImageStorageService imageStorageService) {
        this.productService = productService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/admin/inventory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam("image") MultipartFile image
    ) {
        String storedImagePath = imageStorageService.storeProductImage(image);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(storedImagePath)
                .toUriString();

        Product product = productService.addProduct(
                name,
                description,
                price,
                quantity,
                imageUrl
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

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Map<String, String>> handleRuntimeErrors(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}