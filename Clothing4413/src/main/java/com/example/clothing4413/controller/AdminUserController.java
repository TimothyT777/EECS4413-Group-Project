package com.example.clothing4413.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clothing4413.dto.AdminUserResponse;
import com.example.clothing4413.dto.UpdateUserRequest;
import com.example.clothing4413.model.Administrator;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<AdminUserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdminUserResponse getUserById(@PathVariable Long id) {
        return toResponse(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public AdminUserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        Users updatedUser = userService.updateUser(
                id,
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return toResponse(updatedUser);
    }

    private AdminUserResponse toResponse(Users user) {
        String userType = (user instanceof Administrator) ? "ADMINISTRATOR" : "CUSTOMER";

        return new AdminUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                userType
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}