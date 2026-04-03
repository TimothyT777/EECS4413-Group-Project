package com.example.clothing4413.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.dto.AuthResponse;
import com.example.clothing4413.dto.LoginRequest;
import com.example.clothing4413.dto.RegisterRequest;
import com.example.clothing4413.model.Administrator;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.service.UserService;

import jakarta.servlet.http.HttpSession;
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

        String userType = (user instanceof Administrator) ? "ADMINISTRATOR" : "CUSTOMER";

        AuthResponse response = new AuthResponse(
                "Registration successful.",
                user.getId(),
                user.getName(),
                user.getEmail(),
                userType
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Users user = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        session.setAttribute("user", user);

        String userType = (user instanceof Administrator) ? "ADMINISTRATOR" : "CUSTOMER";

        AuthResponse response = new AuthResponse(
                "Login successful.",
                user.getId(),
                user.getName(),
                user.getEmail(),
                userType
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }

        String userType = (user instanceof Administrator) ? "ADMINISTRATOR" : "CUSTOMER";

        return ResponseEntity.ok(
                new AuthResponse("OK", user.getId(), user.getName(), user.getEmail(), userType)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}