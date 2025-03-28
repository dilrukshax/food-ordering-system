package com.foodordering.userservice.controller;

import com.foodordering.userservice.model.User;
import com.foodordering.userservice.service.UserService;
import com.foodordering.userservice.util.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

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
            String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole());
            return ResponseEntity.ok(token);  // Return the generated token
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");  // Invalid credentials
        }
    }

    // Get All Users (Admin only)
    @GetMapping("/admin/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User by ID (Admin and self-access)
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId, @RequestParam Long requestingUserId, @RequestParam String role) {
        Optional<User> user = userService.getUserById(userId, requestingUserId, role);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(403).build());
    }
}
