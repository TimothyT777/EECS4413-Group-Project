package com.example.clothing4413.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clothing4413.dto.AdminUserResponse;
import com.example.clothing4413.dto.OrderItemResponse;
import com.example.clothing4413.dto.OrderResponse;
import com.example.clothing4413.dto.ProductResponse;
import com.example.clothing4413.dto.UpdateUserRequest;
import com.example.clothing4413.model.Administrator;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Order;
import com.example.clothing4413.model.OrderItem;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.service.OrderService;
import com.example.clothing4413.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final OrderService orderService;

    public AdminUserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public List<AdminUserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::toBasicResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdminUserResponse getUserById(@PathVariable Long id) {
        return toDetailedResponse(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public AdminUserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        Users updatedUser = userService.updateUser(id, request);
        return toDetailedResponse(updatedUser);
    }

    private AdminUserResponse toBasicResponse(Users user) {
        String userType = (user instanceof Administrator) ? "ADMINISTRATOR" : "CUSTOMER";

        return new AdminUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                userType
        );
    }

    private AdminUserResponse toDetailedResponse(Users user) {
        AdminUserResponse response = toBasicResponse(user);

        if (user instanceof Customer customer) {
            response.setShippingAddress(customer.getShippingAddress());
            response.setBillingAddress(customer.getBillingAddress());
            response.setCardHolderName(customer.getCardHolderName());
            response.setCardNumber(customer.getCardNumber());
            response.setCardExpiry(customer.getCardExpiry());

            List<OrderResponse> orders = orderService.getOrderByCustomerId(customer.getId())
                    .stream()
                    .map(this::buildOrderResponse)
                    .collect(Collectors.toList());

            response.setOrders(orders);
        } else {
            response.setOrders(Collections.emptyList());
        }

        return response;
    }

    private OrderResponse buildOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getProduct().getProduct_id());
            productResponse.setName(item.getProduct().getName());
            productResponse.setBrand(item.getProduct().getBrand());
            productResponse.setCategory(item.getProduct().getCategory());
            productResponse.setDescription(item.getProduct().getDescription());
            productResponse.setPrice(item.getProduct().getPrice());
            productResponse.setImage(item.getProduct().getImage());
            productResponse.setStock(item.getProduct().getStock());

            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProduct(productResponse);
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());

            itemResponses.add(itemResponse);
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setItems(itemResponses);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}