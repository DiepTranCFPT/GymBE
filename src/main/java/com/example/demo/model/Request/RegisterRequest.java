package com.example.demo.model.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
//    long locationId;
    String name;
    String password;
    String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email;
}
