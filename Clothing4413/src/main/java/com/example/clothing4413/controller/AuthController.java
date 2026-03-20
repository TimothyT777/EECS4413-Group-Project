package com.example.clothing4413.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.clothing4413.dto.AuthResponse;
import com.example.clothing4413.dto.LoginRequest;
import com.example.clothing4413.dto.RegisterRequest;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Users user = userService.registerCustomer(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        AuthResponse response = new AuthResponse(
                "Registration successful.",
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Users user = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        AuthResponse response = new AuthResponse(
                "Login successful.",
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}