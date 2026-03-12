package com.example.placeholdername.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.placeholdername.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findByEmail(String email);

    List<Users> findByName(String name);
    
}