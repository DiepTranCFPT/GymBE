package com.gymsystem.cyber.IService;

import com.google.firebase.auth.FirebaseAuthException;
import com.gymsystem.cyber.model.Request.LoginRequest;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

public interface IAuthentication {

    CompletableFuture<ResponseObject> login(LoginRequest loginRequest);

    CompletableFuture<ResponseObject> register(RegisterRequest registerRequest) throws AccountNotFoundException;

    CompletableFuture<ResponseObject> Oath (String token) throws FirebaseAuthException;

//    CompletableFuture<ResponseObject> registerFaceId(String idUser, byte[] face);

}
