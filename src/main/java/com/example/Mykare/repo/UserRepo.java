package com.example.Mykare.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Mykare.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

    User findByEmail(String email);
}