package com.example.demo.service;

import com.example.demo.entity.User;

import com.example.demo.enums.UserRole;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.GlobalException;

import com.example.demo.model.EmailDetail;
import com.example.demo.model.Request.*;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.utils.AccountUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AccountUtils accountUtils;

    @Transactional
    public User register(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setRole(UserRole.ADMIN);
        user.setEnable(true);
        user.setVerificationCode(UUID.randomUUID().toString());

        authenticationRepository.save(user);

        try {
            user = authenticationRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Duplicate");
        }


        return user;
    }
    public User registerStaff(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setRole(UserRole.STAFF);
        user.setEnable(true);
        user.setCreateBy("ADMIN");
        user.setCreateTime(LocalDateTime.now());
        user.setVerificationCode(UUID.randomUUID().toString());

        authenticationRepository.save(user);

        try {
            user = authenticationRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Duplicate");
        }

        return user;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public User registerPT(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setRole(UserRole.PT);
        user.setEnable(true);
        user.setCreateBy("ADMIN");
        user.setCreateTime(LocalDateTime.now());
        user.setVerificationCode(UUID.randomUUID().toString());

        authenticationRepository.save(user);

        try {
            user = authenticationRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Duplicate");
        }
        return user;
    }


    public AccountResponse login(LoginRequest loginRequest) {
        var account = authenticationRepository.findByEmail(loginRequest.getEmail());

        if (account == null) {
            throw new AuthException("Account not found with email: " + loginRequest.getEmail());
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new AuthException("Wrong Id Or Password");
        }
        if (!account.isEnable()) {
            throw new AuthException("Account not verified. Please check your email to verify your account.");
        }
        String token = tokenService.generateToken(account);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setToken(token);
        accountResponse.setName(account.getName());
        accountResponse.setPhone(account.getPhone());

        return accountResponse;
    }


}
