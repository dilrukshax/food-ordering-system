package com.foodordering.userservice.controller;

import com.foodordering.userservice.model.User;
import com.foodordering.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Sign-Up endpoint
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    // Sign-In endpoint
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.signIn(email, password);
        if (user.isPresent()) {
            return ResponseEntity.ok("Sign-in successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Get All Users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}