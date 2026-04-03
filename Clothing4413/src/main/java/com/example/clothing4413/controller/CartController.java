package com.example.clothing4413.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.clothing4413.dto.CartItemResponse;
import com.example.clothing4413.dto.CartRequest;
import com.example.clothing4413.dto.CartResponse;
import com.example.clothing4413.dto.ProductResponse;
import com.example.clothing4413.model.Administrator;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.service.CartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId, HttpSession session) {
        verifyCustomerAccess(customerId, session);
        Cart cart = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProduct(@RequestBody CartRequest request, HttpSession session) {
        verifyCustomerAccess(request.getCustomerId(), session);
        Cart cart = cartService.addProductToCart(
                request.getCustomerId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeProduct(@RequestBody CartRequest request, HttpSession session) {
        verifyCustomerAccess(request.getCustomerId(), session);
        Cart cart = cartService.removeProductFromCart(
                request.getCustomerId(),
                request.getProductId()
        );
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(@RequestBody CartRequest request, HttpSession session) {
        verifyCustomerAccess(request.getCustomerId(), session);
        Cart cart = cartService.updateProductQuantityInCart(
                request.getCustomerId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<CartResponse> clearCart(@PathVariable Long customerId, HttpSession session) {
        verifyCustomerAccess(customerId, session);
        Cart cart = cartService.clearCart(customerId);
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    private void verifyCustomerAccess(Long customerId, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in.");
        }

        if (user instanceof Administrator) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrators cannot use the cart.");
        }

        if (!user.getId().equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own cart.");
        }
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getProduct().getProduct_id());
            productResponse.setName(item.getProduct().getName());
            productResponse.setBrand(item.getProduct().getBrand());
            productResponse.setCategory(item.getProduct().getCategory());
            productResponse.setDescription(item.getProduct().getDescription());
            productResponse.setStock(item.getProduct().getStock());
            productResponse.setPrice(item.getProduct().getPrice());
            productResponse.setImage(item.getProduct().getImage());

            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProduct(productResponse);
            itemResponse.setQuantity(item.getQuantity());

            itemResponses.add(itemResponse);
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setItems(itemResponses);

        return cartResponse;
    }
}