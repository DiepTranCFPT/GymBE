package com.gymsystem.cyber.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.gymsystem.cyber.IService.IAuthentication;
import com.gymsystem.cyber.IService.IFaceRecodeService;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.model.Request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
@RequestMapping("api/authen")
@Tag(name = "User Controller", description = "Quản lý các hoạt động người dùng như tạo mới, cập nhật, xóa, xác minh, v.v.")
public class AuthenticationController {

    private final IAuthentication authenticationService;
    private final IFaceRecodeService iFaceRecodeService;

    @Autowired
    public AuthenticationController(IAuthentication authenticationService, IFaceRecodeService iFaceRecodeService) {
        this.authenticationService = authenticationService;
        this.iFaceRecodeService = iFaceRecodeService;
    }


    @Operation(summary = "Tạo người dùng mới", description = "Đăng ký một người dùng mới với thông tin đã cung cấp.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/register")
    public CompletableFuture<ResponseObject> regisAcount(@RequestBody RegisterRequest registerRequest) throws AccountNotFoundException {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "đang nhap (moi quyen)")
    public CompletableFuture<ResponseObject> loginAccount(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping(value = "{id}/register-faceid" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Đăng ký người dùng với faceid", description = "Đăng ký một người dùng mới với faceid.")
    public CompletableFuture<ResponseObject> registerFaceId(@PathVariable("id") String id,
                                                            @RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.regisFaceIDforAccount(id, file);
    }


    @PostMapping("/oath/login/google")
    @Operation(summary = "Đăng nhập với google", description = "Đăng nhập người dùng với tài khoản google.")
    public CompletableFuture<ResponseObject> loginGoogle(@RequestHeader("idToken") String Idtoken) throws FirebaseAuthException {
        System.out.println(Idtoken);
        return authenticationService.Oath(Idtoken);
    }


}
