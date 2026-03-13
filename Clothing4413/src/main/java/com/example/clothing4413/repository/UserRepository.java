package com.example.clothing4413.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clothing4413.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findByEmail(String email);

    List<Users> findByName(String name);
    
}