package com.example.clothing4413.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.dto.CheckoutRequest;
import com.example.clothing4413.dto.CustomerInfoResponse;
import com.example.clothing4413.dto.OrderItemResponse;
import com.example.clothing4413.dto.OrderResponse;
import com.example.clothing4413.dto.ProductResponse;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Order;
import com.example.clothing4413.model.OrderItem;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.UserRepository;
import com.example.clothing4413.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        Order order = orderService.checkout(request);
        return ResponseEntity.ok(buildOrderResponse(order));
    }

    //Get the saved customer billing info
    @GetMapping("/customer-info/{customerId}")
    public ResponseEntity<CustomerInfoResponse> getCustomerInfo(@PathVariable Long customerId) {
        Users user = userRepository.findUsersById(customerId);
        if (!(user instanceof Customer)) {
            throw new IllegalStateException("User is not a customer");
        }

        Customer customer = (Customer) user;
        CustomerInfoResponse info = new CustomerInfoResponse();
        info.setShippingAddress(customer.getShippingAddress());
        info.setBillingAddress(customer.getBillingAddress());
        info.setCardHolderName(customer.getCardHolderName());
        info.setCardNumber(customer.getCardNumber());
        info.setCardExpiry(customer.getCardExpiry());
        info.setHasSavedInfo(customer.getShippingAddress() != null); //This can be done with any of the billing fields. If the information is saved, then obviously the field is not null, and as such the customer clicked on saving the information
        return ResponseEntity.ok(info);
    }

    private OrderResponse buildOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            //Build a product that is inside the order
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getProduct().getProduct_id());
            productResponse.setName(item.getProduct().getName());
            productResponse.setBrand(item.getProduct().getBrand());
            productResponse.setCategory(item.getProduct().getCategory());
            productResponse.setDescription(item.getProduct().getDescription());
            productResponse.setPrice(item.getProduct().getPrice());
            productResponse.setImage(item.getProduct().getImage());

            //Build an item that has been checked out in the order (product, quantity, subtotal)
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProduct(productResponse);
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());

            //Add to list of items in the order
            itemResponses.add(itemResponse);
        }

        //Build the full checked out order
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setItems(itemResponses);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable Long customerId) {
        //Get all orders a customer has submit (or checked out)
        List<Order> orders = orderService.getOrderByCustomerId(customerId);

        //Store these orders as OrderResponses for frontend
        List<OrderResponse> responses = new ArrayList<>();

        //Build every single order the customer has made as an OrderResponse, and add them to a list
        for (Order order : orders) {
            responses.add(buildOrderResponse(order));
        }
        return ResponseEntity.ok(responses);
    }
}