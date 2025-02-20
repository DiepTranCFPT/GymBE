package com.gymsystem.cyber.model.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull
    String email;
    @NotNull
    String password;
}
