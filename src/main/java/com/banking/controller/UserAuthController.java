package com.banking.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.services.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserAuthController {

    private final UserAuthService userAuthService;

    // Constructor-based dependency injection
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * Handles user registration and returns a token upon success.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserDTO userData) {
        String token = userAuthService.registerUser(
                userData.getFirstName(),
                userData.getLastName(),
                userData.getEmail(),
                userData.getPassword()
        );
        return ResponseEntity.ok(token);
    }

    /**
     * Authenticates the user and returns a response map.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody UserDTO userData) throws Exception {
        Map<String, Object> authResponse = userAuthService.verifyUser(
                userData.getEmail(),
                userData.getPassword()
        );
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Data Transfer Object for User Requests.
     */
    public static class UserDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    
}
