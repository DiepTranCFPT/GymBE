package com.example.demo.IService;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.model.Request.LoginRequest;
import com.example.demo.model.Request.RegisterRequest;
import com.example.demo.model.Request.RegisterRequestPT;
import com.example.demo.model.Response.AccountResponse;

public interface IAuthentication {
    Trainer registerPT(RegisterRequestPT registerRequest);
    User registerStaff(RegisterRequest registerRequest);
    AccountResponse login(LoginRequest loginRequest);
}
