package com.example.Mykare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import com.example.Mykare.model.User;
import com.example.Mykare.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Map<String, Object> registerUser(User user) {
        Map<String, Object> res = new HashMap<>();
        
        if (repo.findByEmail(user.getEmail()) != null) {
            res.put("message", "Email already exists!");
            res.put("status", "error");
            return res;
        }
        
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        res.put("message", "User registered successfully!");
        res.put("status", "success");
        
        return res;
    }
    

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        } else {
            return "fail";
        }
    }

    public User getUserByEmail(String email) {
        System.out.println(email);
        return repo.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public void deleteUser(String email) {
        User user = repo.findByEmail(email);
        if (user != null) {
            repo.delete(user);
        }
    }


    public Object findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
