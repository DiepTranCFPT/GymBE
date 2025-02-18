package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.IService.IAuthentication;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.model.Request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
@RequestMapping("api/authen")
@Tag(name = "User Controller", description = "Quản lý các hoạt động người dùng như tạo mới, cập nhật, xóa, xác minh, v.v.")
public class AuthenticationController {

    private final IAuthentication authenticationService;

    @Autowired
    public AuthenticationController(IAuthentication authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Operation(summary = "Tạo người dùng mới", description = "Đăng ký một người dùng mới với thông tin đã cung cấp.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public CompletableFuture<ResponseObject> register(@RequestBody RegisterRequest registerRequest) throws AccountNotFoundException {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "đang nhap (moi quyen)")
    public CompletableFuture<ResponseObject> loginAccount(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

}
