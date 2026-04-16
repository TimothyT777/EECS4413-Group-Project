package com.example.clothing4413.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.dto.CartItemResponse;
import com.example.clothing4413.dto.CartRequest;
import com.example.clothing4413.dto.CartResponse;
import com.example.clothing4413.dto.ProductResponse;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.service.CartService;
import com.example.clothing4413.service.ProductService;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    //Get Cart by customer id
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
        Cart cart = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    //Add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProduct(@RequestBody CartRequest request) {
        Product product = productService.findProductById(request.getProductId());
        if (product.getStock() <= 0) { //Dont add if it is out of stock
            return ResponseEntity.badRequest().body(null);
        } else { //Otherwise add to cart as normal
            Cart cart = cartService.addProductToCart(request.getCustomerId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(buildCartResponse(cart));
        }
    }

    //Remove item from cart
    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeProduct(@RequestBody CartRequest request) {
        Cart cart = cartService.removeProductFromCart(request.getCustomerId(), request.getProductId());
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    //Update quanity of item in cart
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(@RequestBody CartRequest request) {
        Cart cart = cartService.updateProductQuantityInCart(request.getCustomerId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    //Clear cart
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<CartResponse> clearCart(@PathVariable Long customerId) {
        Cart cart = cartService.clearCart(customerId);
        return ResponseEntity.ok(buildCartResponse(cart));
    }

    //Helper to conver Cart to CartResponse so that DTO is sent to frontend instead of the entity.
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