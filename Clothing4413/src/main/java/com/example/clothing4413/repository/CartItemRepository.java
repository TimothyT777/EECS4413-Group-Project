package com.example.clothing4413.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clothing4413.model.CartItem;
import com.example.clothing4413.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByProduct(Product product);
}