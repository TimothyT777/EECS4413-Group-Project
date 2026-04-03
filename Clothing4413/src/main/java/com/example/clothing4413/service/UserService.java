package com.example.clothing4413.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clothing4413.dto.UpdateUserRequest;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.CartRepository;
import com.example.clothing4413.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public Users findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<Users> findByName(String name) {
        return userRepo.findByName(name);
    }

    public boolean userExists(Long id) {
        return userRepo.existsById(id);
    }

    public Users findUsersById(Long id) {
        return userRepo.findUsersById(id);
    }

    public Users getUserById(Long id) {
        Users user = userRepo.findUsersById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return user;
    }

    @Transactional
    public Users registerCustomer(String name, String email, String password) {
        if (userRepo.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Customer customer = new Customer(
                name,
                email,
                passwordEncoder.encode(password)
        );

        Customer savedCustomer = (Customer) userRepo.saveAndFlush(customer);

        Cart cart = new Cart(savedCustomer);
        cartRepository.saveAndFlush(cart);

        return savedCustomer;
    }

    public Users login(String email, String password) {
        Users user = userRepo.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }

    @Transactional
    public Users updateUser(Long id, UpdateUserRequest request) {
        Users user = userRepo.findUsersById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank() && !request.getEmail().equals(user.getEmail())) {
            Users existingUser = userRepo.findByEmail(request.getEmail());

            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (user instanceof Customer customer) {
            if (request.getShippingAddress() != null) {
                customer.setShippingAddress(normalizeNullableField(request.getShippingAddress()));
            }

            if (request.getBillingAddress() != null) {
                customer.setBillingAddress(normalizeNullableField(request.getBillingAddress()));
            }

            if (request.getCardHolderName() != null) {
                customer.setCardHolderName(normalizeNullableField(request.getCardHolderName()));
            }

            if (request.getCardNumber() != null) {
                customer.setCardNumber(normalizeNullableField(request.getCardNumber()));
            }

            if (request.getCardExpiry() != null) {
                customer.setCardExpiry(normalizeNullableField(request.getCardExpiry()));
            }
        }

        return userRepo.save(user);
    }

    private String normalizeNullableField(String value) {
        return value != null && value.isBlank() ? null : value;
    }
}
