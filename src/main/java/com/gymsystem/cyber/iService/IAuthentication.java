package com.gymsystem.cyber.iService;

import com.google.firebase.auth.FirebaseAuthException;
import com.gymsystem.cyber.model.Request.*;
import com.gymsystem.cyber.model.Response.AccountResponse;
import com.gymsystem.cyber.model.Response.UserRespone;
import com.gymsystem.cyber.model.ResponseObject;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

public interface IAuthentication {

    CompletableFuture<ResponseObject> login(LoginRequest loginRequest);
    CompletableFuture<ResponseObject> register(RegisterRequest registerRequest) throws AccountNotFoundException;
    CompletableFuture<ResponseObject> loginByGoogle(LoginGoogleRequest loginGoogleRequest) throws AccountNotFoundException;

    CompletableFuture<ResponseObject> Oath (String token) throws FirebaseAuthException;

    CompletableFuture<ResponseObject> GetAll ();

    String edit ( UserRespone userRespone) throws AccountNotFoundException;
    String delete (String id) throws AccountNotFoundException;


    CompletableFuture<ResponseObject> editUserInfor(String id, TypeEditUser typeEditUser, String content);


    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws AccountNotFoundException;

    int resetPassword(ResetPasswordRequest resetPasswordRequest) throws AccountNotFoundException;
//    CompletableFuture<ResponseObject> registerFaceId(String idUser, byte[] face);

}