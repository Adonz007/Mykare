package com.example.Mykare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Mykare.model.User;
import com.example.Mykare.service.JWTService;
import com.example.Mykare.service.UserService;

@RestController
public class UserController {


    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @PostMapping("/register")
    public Map<String,Object> registerUser(@RequestBody User user) {
        return  userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null && encoder.matches(user.getPassword(), existingUser.getPassword())) {
            String token =  jwtService.generateToken(user.getEmail());
            res.put("Status", "user validated");
            res.put("token", token);
            return res;
        }else{
            res.put("Status", "Invalid credentials!");
            return res ;
        }
       
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
    List<User> users =  userService.getAllUsers();
    //to remove password from the response for now
    List<Map<String, Object>> users_list = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> res = new HashMap<>();
            res.put("Name", user.getName());
            res.put("Gender", user.getGender());
            res.put("Email", user.getEmail());
            users_list.add(res);
        }
        return users_list;
    }

    @DeleteMapping("/delete")
    public Map<String, String> deleteUser(@RequestParam String email) {
        Map<String, String> res = new HashMap<>();
    
        // Check if user with the given email exists in the database
        if (userService.findByEmail(email) == null) {
            // If user does not exist, return error message
            res.put("message", "Email does not exist!");
            res.put("status", "error");
            return res;
        }
    
        // Proceed with deleting the user if they exist
        userService.deleteUser(email);
    
        // Return success message if user was deleted
        res.put("message", "User deleted successfully!");
        res.put("status", "success");
        return res;
    }
    
    
}
