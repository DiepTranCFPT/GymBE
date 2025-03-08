package com.gymsystem.cyber.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.gymsystem.cyber.iService.IAuthentication;
import com.gymsystem.cyber.iService.IFaceRecodeService;
import com.gymsystem.cyber.model.Request.LoginGoogleRequest;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.Response.AccountResponse;
import com.gymsystem.cyber.model.Response.UserRespone;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.model.Request.LoginRequest;
import com.gymsystem.cyber.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
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


    @PostMapping("/firebase-login")
    public CompletableFuture<ResponseObject> firebaseLogin(@RequestBody Map<String, String> request) throws FirebaseAuthException {
        String token = request.get("token");
        System.out.println(token);
        return authenticationService.Oath(token);
    }

    @PostMapping(value = "register-faceid/{email}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Đăng ký người dùng với faceid", description = "Đăng ký một người dùng mới với faceid.")
    public CompletableFuture<ResponseObject> registerFaceId(@PathVariable("email") String id,
                                                            @RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.regisFaceIDforAccount(id, file);
    }

    @PostMapping(value = "face-login", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "login faceid")
    public CompletableFuture<ResponseObject> login(@RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.loginFaceID(file);
    }
    @PostMapping(value = "face-logout", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "logout faceid")
    public CompletableFuture<ResponseObject> logout(@RequestParam("file") MultipartFile file) throws AccountNotFoundException, IOException {
        return iFaceRecodeService.loginFaceID(file);
    }

    @GetMapping("/profile")
    @Operation(summary = "đang nhap (moi quyen)")
    public String profile(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));
        model.addAttribute("photo", token.getPrincipal().getAttribute("picture"));
        System.out.println("token.getPrincipal().getAttribute()");
        return "user-profile";
    }

    @GetMapping("/signup-with-google")
    @Operation(summary = "đang nhap (moi quyen)")
    public CompletableFuture<ResponseObject> signupWithGoogle(OAuth2AuthenticationToken authenticationToken) throws AccountNotFoundException {
        Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();
        LoginGoogleRequest googleLoginRequest = new LoginGoogleRequest();
        googleLoginRequest.setEmail((String) attributes.get("email"));
        googleLoginRequest.setName((String) attributes.get("name"));
        System.out.println("hihihihihi");
        System.out.println(googleLoginRequest);
        return authenticationService.loginByGoogle(googleLoginRequest);
    }

    @GetMapping("/get-all")
    public CompletableFuture<ResponseObject> getAll() {
        return authenticationService.GetAll();
    }

    @PutMapping("/edit")
    public String edit(@RequestBody UserRespone userRespone) throws AccountNotFoundException {
        return authenticationService.edit(userRespone);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String id) throws AccountNotFoundException {
        return authenticationService.delete(id);
    }
}
