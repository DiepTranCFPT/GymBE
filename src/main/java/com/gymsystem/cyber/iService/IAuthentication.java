package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.LoginRequest;
import com.gymsystem.cyber.model.Request.RegisterRequest;
import com.gymsystem.cyber.model.ResponseObject;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

public interface IAuthentication {

    CompletableFuture<ResponseObject> login(LoginRequest loginRequest);
    CompletableFuture<ResponseObject> register(RegisterRequest registerRequest) throws AccountNotFoundException;
}
