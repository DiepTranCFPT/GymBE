package com.example.demo.service;

import com.example.demo.IService.IAuthentication;
import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;

import com.example.demo.enums.UserRole;
import com.example.demo.exception.AuthException;
import com.example.demo.model.Request.*;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.TrainerRepository;
import com.example.demo.utils.AccountUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AuthenticationService implements IAuthentication {

    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    AccountUtils accountUtils;

    @Transactional
    public User register(@NotNull RegisterRequest registerRequest) {
        User existingUserByEmail = authenticationRepository.findByEmail(registerRequest.getEmail());
        User existingUserByPhone = authenticationRepository.findByPhone(registerRequest.getPhone());

        if (existingUserByEmail != null) {
            throw new AuthException("Email already in use.");
        }
        if (existingUserByPhone != null) {
            throw new AuthException("Phone number already in use.");
        }
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
    public User registerStaff(@NotNull RegisterRequest registerRequest) {
        User existingUser = authenticationRepository.findByEmail(registerRequest.getEmail());
        if (existingUser == null) {
            existingUser = authenticationRepository.findByPhone(registerRequest.getPhone());
        }
        if(existingUser!=null){
            existingUser.setLastUpdatedTime(LocalDateTime.now());
            existingUser.setRole(UserRole.STAFF);
            return existingUser;
        }
        else {
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
            return user;

        }

    }

    public Trainer registerPT(@NotNull RegisterRequestPT registerRequest) {
        User existingUser = authenticationRepository.findByEmail(registerRequest.getEmail());
        if (existingUser == null) {
            existingUser = authenticationRepository.findByPhone(registerRequest.getPhone());
        }

        if (existingUser != null) {
            existingUser.setName(registerRequest.getName());
            existingUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            existingUser.setRole(UserRole.PT);
            existingUser.setCreateBy("ADMIN");
            existingUser.setLastUpdatedTime(LocalDateTime.now());
            authenticationRepository.save(existingUser);
            Trainer trainer = new Trainer();

            trainer.setUser(existingUser);
            trainer.setExperience_year(registerRequest.getExperienceYear());
            trainer.setAvailability(true);
            trainer.setSpecialization(registerRequest.getSpeciliation());
            trainerRepository.save(trainer);

            return trainer;
        } else {
            User newUser = new User();
            newUser.setName(registerRequest.getName());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setPhone(registerRequest.getPhone());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setRole(UserRole.PT);
            newUser.setEnable(true);
            newUser.setCreateBy("ADMIN");
            newUser.setCreateTime(LocalDateTime.now());
            newUser.setVerificationCode(UUID.randomUUID().toString());
            authenticationRepository.save(newUser);

            Trainer trainer = new Trainer();
            trainer.setUser(existingUser);
            trainer.setExperience_year(registerRequest.getExperienceYear());
            trainer.setAvailability(true);
            trainer.setSpecialization(registerRequest.getSpeciliation());
            trainerRepository.save(trainer);

            return trainer;
        }
    }




    public AccountResponse login(@NotNull LoginRequest loginRequest) {
        var account = authenticationRepository.findByEmail(loginRequest.getEmail());

        if (account.getRole().name().equals("USER")) {
            throw new AuthException("Account not access denied ");
        }
        if (account.getEmail() == null) {
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
