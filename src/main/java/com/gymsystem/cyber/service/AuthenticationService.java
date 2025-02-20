package com.gymsystem.cyber.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.gymsystem.cyber.IService.IAuthentication;
import com.gymsystem.cyber.entity.User;

import com.gymsystem.cyber.enums.UserRole;
import com.gymsystem.cyber.model.Response.LoginReponse;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.TrainerRepository;

import com.gymsystem.cyber.model.Request.LoginRequest;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;


@Service
public class AuthenticationService implements IAuthentication {


    private final AuthenticationRepository authenticationRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final TrainerRepository trainerRepository;


    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository,
                                 TokenService tokenService,
                                 PasswordEncoder passwordEncoder,
                                 TrainerRepository trainerRepository
    ) {
        this.authenticationRepository = authenticationRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.trainerRepository = trainerRepository;
    }


    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseObject> login(LoginRequest loginRequest) {

        User user = authenticationRepository.findByEmail(loginRequest.getEmail())
                .map(user1 -> {
                    if (user1.isDeleted()) {
                        throw new UsernameNotFoundException("Account has been deleted!");
                    }
                    if (!user1.isEnable()) {
                        throw new UsernameNotFoundException("Account is not enabled!");
                    }
                    if (!passwordEncoder.matches(loginRequest.getPassword(), user1.getPassword()))
                        throw new UsernameNotFoundException("Incorrect password!");
                    return user1;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Account is not exists!"));


        return CompletableFuture.supplyAsync(() -> {

            LoginReponse accountResponse = LoginReponse.builder()
                    .name(user.getName())
                    .token(tokenService.generateToken(user))
                    .phone(user.getPhone() == null ? "" : user.getPhone())
                    .email(user.getEmail())
                    .id(user.getId())
                    .build();
            return ResponseObject.builder()
                    .data(accountResponse)
                    .message("Login successful")
                    .httpStatus(HttpStatus.OK)
                    .build();
        });
    }

    @Transactional
    @Override
    @Async
    public CompletableFuture<ResponseObject> register(RegisterRequest registerRequest) throws AccountNotFoundException {

        boolean check = authenticationRepository.existsByEmail(registerRequest.getEmail());
        System.out.println(check);
        if (check) {
            throw new AccountNotFoundException("email exists!");
        }

        return CompletableFuture.supplyAsync(() -> {
            User user = User.builder()
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail() == null ? "" : registerRequest.getEmail())
                    .role(UserRole.USER)
//                    .phone(registerRequest.getPhone() == null ? "" : registerRequest.getPhone())
                    .enable(true)
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();

            authenticationRepository.saveAndFlush(user);

            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("register successfully!")
                    .data(true)
                    .build();
        });
    }

    @Override
    public CompletableFuture<ResponseObject> Oath(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);

        String email = firebaseToken.getEmail();

        var user = authenticationRepository.findByEmail(email).map(user1 -> {
            if (user1.isDeleted()) {
                throw new UsernameNotFoundException("Account deleted");
            }
            if (!user1.isEnable()) {
                throw new UsernameNotFoundException("Account is Enable!");
            }
            return user1;
        }).get();

        if (user != null) {
            return CompletableFuture.completedFuture(ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .data(LoginReponse.builder()
                            .email(user.getEmail())
                            .name(user.getName())
                            .phone(user.getPhone())
                            .id(user.getId())
                            .token(tokenService.generateToken(user))
                            .build())
                    .build());
        }
        User temp = User.builder()
                .firebaseUid(firebaseToken.getUid())
                .email(email)
                .name(firebaseToken.getName())
                .role(UserRole.USER)
                .build();

        authenticationRepository.saveAndFlush(temp);

        return CompletableFuture.completedFuture(ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .data(LoginReponse.builder()
                        .email(temp.getEmail())
                        .name(temp.getName())
                        .phone(temp.getPhone())
                        .id(temp.getId())
                        .token(tokenService.generateToken(temp))
                        .build())
                .build());
    }


//    public User registerStaff(RegisterRequest registerRequest) {
//        User existingUser = authenticationRepository.findByEmail(registerRequest.getEmail());
//        if (existingUser == null) {
//            existingUser = authenticationRepository.findByPhone(registerRequest.getPhone());
//        }
//        if (existingUser != null) {
//            existingUser.setLastUpdatedTime(LocalDateTime.now());
//            existingUser.setRole(UserRole.STAFF);
//            return existingUser;
//        } else {
//            User user = new User();
//            user.setName(registerRequest.getName());
//            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//            user.setPhone(registerRequest.getPhone());
//            user.setEmail(registerRequest.getEmail());
//            user.setRole(UserRole.STAFF);
//            user.setEnable(true);
//            user.setCreateBy("ADMIN");
//            user.setCreateTime(LocalDateTime.now());
//            user.setVerificationCode(UUID.randomUUID().toString());
//            authenticationRepository.save(user);
//            return user;
//
//        }
//
//    }
//
//    public Trainer registerPT(RegisterRequestPT registerRequest) {
//        User existingUser = authenticationRepository.findByEmail(registerRequest.getEmail());
//        if (existingUser == null) {
//            existingUser = authenticationRepository.findByPhone(registerRequest.getPhone());
//        }
//
//        if (existingUser != null) {
//            existingUser.setName(registerRequest.getName());
//            existingUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//            existingUser.setRole(UserRole.PT);
//            existingUser.setCreateBy("ADMIN");
//            existingUser.setLastUpdatedTime(LocalDateTime.now());
//            authenticationRepository.save(existingUser);
//            Trainer trainer = new Trainer();
//
//            trainer.setUser(existingUser);
//            trainer.setExperience_year(registerRequest.getExperienceYear());
//            trainer.setAvailability(true);
//            trainer.setSpecialization(registerRequest.getSpeciliation());
//            trainerRepository.save(trainer);
//
//            return trainer;
//        } else {
//            User newUser = new User();
//            newUser.setName(registerRequest.getName());
//            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//            newUser.setPhone(registerRequest.getPhone());
//            newUser.setEmail(registerRequest.getEmail());
//            newUser.setRole(UserRole.PT);
//            newUser.setEnable(true);
//            newUser.setCreateBy("ADMIN");
//            newUser.setCreateTime(LocalDateTime.now());
//            newUser.setVerificationCode(UUID.randomUUID().toString());
//            authenticationRepository.save(newUser);
//
//            Trainer trainer = new Trainer();
//            trainer.setUser(existingUser);
//            trainer.setExperience_year(registerRequest.getExperienceYear());
//            trainer.setAvailability(true);
//            trainer.setSpecialization(registerRequest.getSpeciliation());
//            trainerRepository.save(trainer);
//
//            return trainer;
//        }
//    }
//
//
//    public AccountResponse login(LoginRequest loginRequest) {
//        var account = authenticationRepository.findByEmailAndAndDeletedIsFalse(loginRequest.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("Account not found or account deleted!"));
//
//
//        String token = tokenService.generateToken(account);
//        AccountResponse accountResponse = new AccountResponse();
//        accountResponse.setId(account.getId());
//        accountResponse.setEmail(account.getEmail());
//        accountResponse.setToken(token);
//        accountResponse.setName(account.getName());
//        accountResponse.setPhone(account.getPhone());
//        return accountResponse;
//    }


}
