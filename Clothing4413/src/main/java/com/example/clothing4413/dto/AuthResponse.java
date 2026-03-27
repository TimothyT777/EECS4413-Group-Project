package com.example.clothing4413.dto;

public class AuthResponse {

    private String message;
    private Long id;
    private String name;
    private String email;

    public AuthResponse() {}

    public AuthResponse(String message, Long id, String name, String email) {
        this.message = message;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}