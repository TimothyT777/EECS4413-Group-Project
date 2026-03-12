package com.example.placeholdername.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.placeholdername.model.Users;
import com.example.placeholdername.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public List<Users> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<Users> findByName(String name) {
        return userRepo.findByName(name);
    }

    public boolean userExists(Long id) {
        return userRepo.existsById(id);
    }
    
    @Transactional
    public void addUser(Users user) {
        //Maybe in the future we can hash user passwords before saving
        userRepo.save(user);
    }

    //I think a seperate method needed for adding Administrators.

}