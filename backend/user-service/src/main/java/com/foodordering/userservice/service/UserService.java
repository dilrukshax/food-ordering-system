package com.foodordering.userservice.service;

import com.foodordering.userservice.model.User;
import com.foodordering.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Sign up new user
    public User signUp(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Sign in user
    public Optional<User> signIn(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty(); // Invalid credentials
    }

    // Get all users (Admin access only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID (accessible to admin and the user itself)
    public Optional<User> getUserById(Long userId, Long requestingUserId, String role) {
        if (role.equals("admin") || requestingUserId.equals(userId)) {
            return userRepository.findById(userId);
        }
        return Optional.empty(); // Unauthorized
    }
}
