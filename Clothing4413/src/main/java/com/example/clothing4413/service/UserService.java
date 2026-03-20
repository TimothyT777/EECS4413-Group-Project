package com.example.clothing4413.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public Users findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    public List<Users> findByName(String name) {
        return userRepo.findByName(name);
    }

    public boolean userExists(Long id) {
        return userRepo.existsById(id);
    }

    @Transactional
    public Users registerCustomer(String name, String email, String password) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Customer customer = new Customer(
                name,
                email,
                passwordEncoder.encode(password)
        );

        return userRepo.save(customer);
    }

    public Users login(String email, String password) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }
}