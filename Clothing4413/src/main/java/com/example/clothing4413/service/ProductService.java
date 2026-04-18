package com.example.clothing4413.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.OrderItem;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.model.ProductCategory;
import com.example.clothing4413.repository.CartItemRepository;
import com.example.clothing4413.repository.OrderItemRepository;
import com.example.clothing4413.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CartItemRepository cartItemRepo;
    private final OrderItemRepository orderItemRepo;

    public ProductService(ProductRepository productRepo, CartItemRepository cartItemRepo, OrderItemRepository orderItemRepo) {
        this.productRepo = productRepo;
        this.cartItemRepo = cartItemRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> findByName(String name) {
        return productRepo.findByName(name);
    }

    public Product findProductById(Long id) {
        return productRepo.findProductById(id);
    }

    public Product addProduct(String name, String description, double price, int quantity, String brand, ProductCategory category, String imageUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Product brand is required.");
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Product image is required.");
        }

        Product product = new Product(name.trim(), description == null ? "" : description.trim(), price, quantity, brand.trim(), category);
        product.setImage(imageUrl);

        return productRepo.save(product);
    }

    public Product updateQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Product product = productRepo.findProductById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        product.setStock(quantity);
        return productRepo.save(product);
    }

    public void clear() {
        productRepo.deleteAll();
    }

    public void removeProductById(Long id) {
        Product product = productRepo.findProductById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        //Dont delete if product has been ordered before
        List<OrderItem> orderItems = orderItemRepo.findByProduct(product);
        if (!orderItems.isEmpty()) {
            throw new IllegalStateException("Cannot delete product that has been ordered before.");
        }

        //Remove product that is being deleted from all carts
        List<CartItem> cartItems = cartItemRepo.findByProduct(product);
        cartItemRepo.deleteAll(cartItems);

        //Remove product from product repository
        productRepo.deleteById(id);
    }

    public boolean productExists(Long id) {
        return productRepo.existsById(id);
    }
}
