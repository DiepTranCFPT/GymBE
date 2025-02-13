package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.Request.*;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
@RequestMapping("admin")

public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    //role -- ADMIN
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
//        User user = authenticationService.register(registerRequest);
//        return ResponseEntity.ok("Success");
//    }
    // Register a Staff user - Only accessible to Admin

    @PostMapping("/register_Staff")
    public ResponseEntity<String> registerStaff(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authenticationService.registerStaff(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Staff user created successfully with ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating staff user: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register_PT")
    public ResponseEntity<String> registerPT(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authenticationService.registerPT(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("PT user created successfully with ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating PT user: " + e.getMessage());
        }
    }

    // Login functionality - accessible by any user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AccountResponse account = authenticationService.login(loginRequest);
            return ResponseEntity.ok(account.getToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }
    }
}
