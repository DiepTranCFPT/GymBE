package com.gymsystem.cyber.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.gymsystem.cyber.iService.IAuthentication;
import com.gymsystem.cyber.iService.IFaceRecodeService;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.Request.TypeEditUser;
import com.gymsystem.cyber.model.Response.UserRespone;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.model.Request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RestController
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
@RequestMapping("api/users")
@Tag(name = "User Controller", description = "Quản lý các hoạt động người dùng như tạo mới, cập nhật, xóa, xác minh, v.v.")
public class AuthenticationController {

    private final IAuthentication authenticationService;
    private final IFaceRecodeService iFaceRecodeService;

    @Autowired
    public AuthenticationController(IAuthentication authenticationService
            , IFaceRecodeService iFaceRecodeService
    ) {
        this.authenticationService = authenticationService;
        this.iFaceRecodeService = iFaceRecodeService;
    }


    @Operation(summary = "Tạo người dùng mới", description = "Đăng ký một người dùng mới với thông tin đã cung cấp.")
    @PostMapping
    public CompletableFuture<ResponseObject> regisAcount(@RequestBody RegisterRequest registerRequest) throws AccountNotFoundException {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "đang nhap (moi quyen)")
    public CompletableFuture<ResponseObject> loginAccount(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }


    @PostMapping("/firebase-login")
    @Operation(summary = "dang nhap voi firebase token")
    public CompletableFuture<ResponseObject> firebaseLogin(@RequestBody Map<String, String> request) throws FirebaseAuthException {
        String token = request.get("token");
        System.out.println(token);
        return authenticationService.Oath(token);
    }

    @PostMapping(value = "/{id}/register-faceid", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Đăng ký người dùng với faceid", description = "Đăng ký một người dùng mới với faceid.")
    public CompletableFuture<ResponseObject> registerFaceId(@PathVariable("id") String id,
                                                            @RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.regisFaceIDforAccount(id, file);
    }

    @PostMapping(value = "/face/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "login faceid")
    public CompletableFuture<ResponseObject> login(@RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.loginFaceID(file);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "lay tat ca nguoi dung co tren he thong (ADMIN)")
    public CompletableFuture<ResponseObject> getAll() {
        return authenticationService.GetAll();
    }

    @PutMapping("/edit")
    @Operation(summary = "sua thong tin tai khoan (ALL ROLE)")
    public String edit(@RequestBody UserRespone userRespone) throws AccountNotFoundException {
        return authenticationService.edit(userRespone);
    }

    @PutMapping("/edit/{id}/{type}")
    @Operation(summary = "thay doi thong tim name & phone (ALL ROLE)")
    public CompletableFuture<ResponseObject> edit(@PathVariable("id") String id, @PathVariable("type") TypeEditUser typeEditUser, String content) {
        return authenticationService.editUserInfor(id, typeEditUser, content);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "khoa va mo khoa tai khoan voi id user hop le (ADMIN)")
    public String delete(@PathVariable("id") String id) throws AccountNotFoundException {
        return authenticationService.delete(id);
    }

    @PostMapping("/sendmail/{email}")
    @Operation(summary = "gui ma xac nhan ve email")
    public CompletableFuture<ResponseObject> sendMail(@PathVariable("email") String email) {
        return authenticationService.sendCode(email);
    }

    @PutMapping("/sendmail/{email}/{code}")
    @Operation(summary = "gui ma xac nhan va password ")
    public CompletableFuture<ResponseObject> sendMail(@PathVariable("email") String email, @PathVariable("code") String code, @RequestBody String newPassword) {
        return CompletableFuture.completedFuture(authenticationService.changePassword(email, code, newPassword));
    }

    @PostMapping("/save-fcm-token/{id}")
    @Operation(summary = "luu Fcm token cua firebase -> gui thong bao")
    public CompletableFuture<ResponseObject> saveFCMToken(@PathVariable("id") String idUser,@RequestBody String token) {
        return authenticationService.saveFcmToken(idUser, token);
    }

}
