package com.gymsystem.cyber.service;


import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.enums.UserRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.gymsystem.cyber.exception.BadRequestException;
import com.gymsystem.cyber.exception.GlobalException;
import com.gymsystem.cyber.iService.IAuthentication;
import com.gymsystem.cyber.exception.AuthException;
import com.gymsystem.cyber.model.EmailDetail;
import com.gymsystem.cyber.model.Request.*;
import com.gymsystem.cyber.model.Response.AccountResponse;
import com.gymsystem.cyber.model.Response.LoginReponse;
import com.gymsystem.cyber.model.Response.UserRespone;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import com.gymsystem.cyber.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;
import java.util.concurrent.CompletionException;



@Service
public class AuthenticationService implements IAuthentication {


    private final AuthenticationRepository authenticationRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final TrainerRepository trainerRepository;

    private final MembershipPlansRepository membershipPlansRepository;

    private final EmailService emailService;


//    private final AccountUtils accountUtils;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository,
                                 TokenService tokenService,
                                 PasswordEncoder passwordEncoder,
                                 TrainerRepository trainerRepository, MembershipPlansRepository membershipPlansRepository, EmailService emailService
//            ,AccountUtils accountUtils

    ) {
        this.authenticationRepository = authenticationRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.trainerRepository = trainerRepository;
        this.membershipPlansRepository = membershipPlansRepository;
        this.emailService = emailService;
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
                    .deleted(false).build();



            authenticationRepository.saveAndFlush(user);

            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("register successfully!")
                    .data(true)
                    .build();
        });
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



    @Override
    public CompletableFuture<ResponseObject> Oath(String token) throws FirebaseAuthException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String email = firebaseToken.getEmail();
                System.out.println(email);

                // Tìm user theo email
                User userOpt = authenticationRepository.findByEmail(email).map(user -> {
                    if (user.isDeleted()) {
                        throw new RuntimeException("account deleted");
                    }
                    if (!user.isEnable()) {
                        throw new UsernameNotFoundException("Account is not enabled!");
                    }
                    return user;
                }).orElse(null);

                if (userOpt != null) {

                    return ResponseObject.builder()
                            .httpStatus(HttpStatus.OK)
                            .data(LoginReponse.builder()
                                    .email(userOpt.getEmail())
                                    .name(userOpt.getName())
                                    .phone(userOpt.getPhone())
                                    .id(userOpt.getId())
                                    .token(tokenService.generateToken(userOpt))
                                    .build())
                            .build();
                }

                // Nếu user không tồn tại, tạo user mới
                User newUser = User.builder()
                        .firebaseUid(firebaseToken.getUid())
                        .email(email)
                        .name(firebaseToken.getName())
                        .role(UserRole.USER)
                        .enable(true).deleted(false).build();
                authenticationRepository.saveAndFlush(newUser);
                return ResponseObject.builder()
                        .httpStatus(HttpStatus.OK)
                        .data(LoginReponse.builder()
                                .email(newUser.getEmail())
                                .name(newUser.getName())
                                .phone(newUser.getPhone())
                                .id(newUser.getId())
                                .token(tokenService.generateToken(newUser))
                                .build())
                        .build();

            } catch (FirebaseAuthException e) {
                throw new CompletionException(e);
            }
        });
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public CompletableFuture<ResponseObject> GetAll() {
        List<User> users = authenticationRepository.findAll();
        List<UserRespone> accountResponses = new ArrayList<>();

        for (User user : users) {
            String planName = "No Plan";

            if(user.getMembers() != null)
                planName = user.getMembers().getSubscriptions().getMemberShipPlans().getName();

            accountResponses.add(UserRespone.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .phone(user.getPhone())
                    .id(user.getId())
                    .enable(user.isEnable())
                    .plan(planName)
                    .build());
        }

        return CompletableFuture.supplyAsync(() -> ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .data(accountResponses)
                .message("Get all successfully!")
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String edit(UserRespone userRespone) throws AccountNotFoundException {
      User user = authenticationRepository.findById(userRespone.getId()).orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
        user.setName(userRespone.getName());
        user.setEmail(userRespone.getEmail());
        user.setPhone(userRespone.getPhone());
        user.setEnable(userRespone.isEnable());
        user.setRole(userRespone.getRole());
        authenticationRepository.saveAndFlush(user);
        return "Success";
    }

    @Override
    public String delete(String id) throws AccountNotFoundException {
        User user = authenticationRepository.findById(id) .orElseThrow(() -> new AccountNotFoundException("Account does not exist"));;
        user.setEnable(false);
        authenticationRepository.saveAndFlush(user);
        return "Success";
    }
    @Transactional
    @Override
    @Async
    public CompletableFuture<ResponseObject> loginByGoogle(LoginGoogleRequest loginGoogleRequest) throws AccountNotFoundException {
        // Tìm kiếm tài khoản bằng email
        User account = authenticationRepository.findByEmail(loginGoogleRequest.getEmail())
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
        // Nếu tài khoản không tồn tại, tạo tài khoản mới
        if (account == null) {
            // Tạo tài khoản mới
            account = User.builder()
                    .name(loginGoogleRequest.getName())
                    .email(loginGoogleRequest.getEmail())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // Sử dụng một password tạm thời
                    .role(UserRole.USER)
                    .enable(true)
                    .verificationCode(UUID.randomUUID().toString())
                    .deleted(false).build();

            // Lưu tài khoản vào cơ sở dữ liệu
            try {
                authenticationRepository.saveAndFlush(account);
            } catch (DataIntegrityViolationException e) {
                throw new AuthException("Duplicate account creation error.");
            }
        }

        if (!account.isEnable()) {
            throw new AuthException("Account not verified. Please check your email to verify your account.");
        }
        String token = tokenService.generateToken(account);

        // Trả về thông tin tài khoản đã đăng nhập
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setToken(token);
        accountResponse.setName(account.getName());
        accountResponse.setPhone(account.getPhone());

        // Trả về thông tin phản hồi
        return CompletableFuture.supplyAsync(() -> {
            return ResponseObject.builder()
                    .data(accountResponse)
                    .message("Login successful")
                    .httpStatus(HttpStatus.OK)
                    .build();
        });
    }
    @Override
    public int resetPassword(ResetPasswordRequest resetPasswordRequest) throws AccountNotFoundException {
        User user = authenticationRepository.findByEmail(resetPasswordRequest.getEmail()).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        String token = tokenService.generateToken(user);
        // Check if the token matches
        if (!token.equals(resetPasswordRequest.getToken())) {
            throw new GlobalException("Invalid token");
        }else {
            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            authenticationRepository.save(user);
            return 1;
        }

    }
    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws AccountNotFoundException {
        User account = authenticationRepository.findByEmail(forgotPasswordRequest.getEmail()).orElseThrow(()-> new AccountNotFoundException("Account not found"));


        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(forgotPasswordRequest.getEmail());
        emailDetail.setSubject("Reset Password for account " + forgotPasswordRequest.getEmail() + "!!!");
        emailDetail.setMsgBody(""); // You might want to add a meaningful message here
        emailDetail.setButtonValue("Reset Password");
        emailDetail.setLink("https://gymbe-production.up.railway.app/api/authen/reset-password?token=" + tokenService.generateToken(account));
        emailDetail.setName(account.getName());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                emailService.sendMailTemplateForgot(emailDetail);
            }
        };

        new Thread(r).start();
    }


}
