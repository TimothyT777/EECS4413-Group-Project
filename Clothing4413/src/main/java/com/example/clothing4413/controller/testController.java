package com.example.clothing4413.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/api/test")
    public String test() {
        return "Hello, World!";
    }
}