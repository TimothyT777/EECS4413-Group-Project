package com.example.clothing4413;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.clothing4413.model.Product;
import com.example.clothing4413.repository.ProductRepository;

@SpringBootApplication(scanBasePackages = "com.example.clothing4413")
public class Clothing4413Application {

    public static void main(String[] args) {
        SpringApplication.run(Clothing4413Application.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository repo) {
    return args -> {
        if (repo.count() == 0) {
            repo.save(new Product("Shirt 1", "This is Shirt 1", 10, "/img/shirt1.png"));
            repo.save(new Product("Shirt 2", "This is Shirt 2", 23.99, "/img/shirt2.png"));
            System.out.println("Loaded presaved products");
        }
    };
}

}
