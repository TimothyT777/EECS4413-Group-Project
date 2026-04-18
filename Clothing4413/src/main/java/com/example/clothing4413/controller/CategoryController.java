package com.example.clothing4413.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.model.ProductCategory;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @GetMapping
    public ProductCategory[] getCategories() {
        return ProductCategory.values();
    }
}
