package com.example.clothing4413.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.clothing4413.model.Administrator;
import com.example.clothing4413.model.Cart;
import com.example.clothing4413.model.Customer;
import com.example.clothing4413.model.Product;
import com.example.clothing4413.model.ProductCategory;
import com.example.clothing4413.repository.CartRepository;
import com.example.clothing4413.repository.ProductRepository;
import com.example.clothing4413.repository.UserRepository;


/***
 * Initializes database with default values
 * Initializes A customer, An admin, and 4 products.
 * If they already exist, it does not add them again.
 * If there are already products, it does not seed products. If there are already users, it does not seed users.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    

    public DataInitializer(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository ,PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Override
    public void run(String ...args) {
        seedUsers();
        seedProducts();
    }

    //Adds default customer and admin
    private void seedUsers() {
        if (userRepository.count() == 0) {
            Customer defaultCustomer = new Customer(
                "John Doe",
                "John@example.com",
                passwordEncoder.encode("customer123")
            );

            Administrator defaultAdmin = new Administrator(
                "Jane Doe",
                "Jane@admin.com",
                passwordEncoder.encode("admin123")
            );

            userRepository.saveAndFlush(defaultCustomer);
            userRepository.saveAndFlush(defaultAdmin);

            //Cart for default customer
            Cart defaultCustomerCart = new Cart(defaultCustomer);
            cartRepository.save(defaultCustomerCart);
            
            System.out.println("Default users seeded");
        } else {
            System.out.println("Users already exist, skipping seeding");
        }
    }

    //Adds 4 default products
    private void seedProducts() {
        if (productRepository.count() == 0) {
            //name, description, price, stock, brand, category, image
            Product shirt1 = new Product("Blue Gucci Shirt", "A light blue Gucci Shirt", 19.99, 5, "Gucci", ProductCategory.SHIRTS, "/img/shirt1.png");
            Product shirt2 = new Product("Navy Gucci Shirt", "A navy blue Gucci Shirt", 23.99, 3, "Gucci", ProductCategory.SHIRTS, "/img/shirt2.png");
            Product shirt3 = new Product("Navy Nike Shirt", "A navy blue Nike Shirt", 27.99, 1, "Nike", ProductCategory.SHIRTS, "/img/shirt3.png");
            Product shirt4 = new Product("Plaid Adidas Shirt", "A red/blue plaidd Adidas Shirt", 12.99, 10, "Adidas", ProductCategory.SHIRTS, "/img/shirt4.png");

            productRepository.saveAndFlush(shirt1);
            productRepository.saveAndFlush(shirt2);
            productRepository.saveAndFlush(shirt3);
            productRepository.saveAndFlush(shirt4);

            System.out.println("Default products seeded");
        } else {
            System.out.println("Products already exist, skipping seeding");
        }
    }
}