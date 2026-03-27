package com.example.clothing4413.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.model.Users;
import com.example.clothing4413.repository.CartRepository;
import com.example.clothing4413.repository.ProductRepository;
import com.example.clothing4413.repository.UserRepository;

@Service
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    //Create cart for customer, do when a new custoemr registers.
    public Cart createCart(Long customerId) {
        Customer customer = getCustomer(customerId);
        if (cartRepository.existsByCustomerId(customerId)) {
            throw new IllegalArgumentException("Customer with id " + customerId + " already has a cart");
        }
        Cart cart = new Cart(customer);
        return cartRepository.save(cart);
    }

    //Get cart
    public Cart getCartByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart for customer with id " + customerId + " not found");
        }
        return cart;
    }

    //Add product to Cart.
    public Cart addProductToCart(Long customerId, Long productId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        cart.addProduct(product, quantity);
        return cartRepository.save(cart);
    }

    //Remove product from Cart.
    public Cart removeProductFromCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        cart.removeProduct(product);
        return cartRepository.save(cart);
    }

    //Update quantity of product in Cart. Set instead of Increment/Deincrement.
    public Cart updateProductQuantityInCart(Long customerId, Long productId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);
        if (quantity <= 0) {
            //Will leave as removal for now.
            cart.removeProduct(productRepository.findProductById(productId));
            return cartRepository.save(cart);
        }

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        cart.updateProductQuantity(product, quantity);
        return cartRepository.save(cart); 
    }

    //Clear cart
    public Cart clearCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    //Helper method to get user as customer.
    private Customer getCustomer(Long customerId) {
        Users user = userRepository.findUsersById(customerId);
        if (user == null) {
            throw new IllegalArgumentException("Customer with id " + customerId + " not found");
        }
        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("User with id " + customerId + " is not a customer");
        }
        return (Customer) user;
    }
}