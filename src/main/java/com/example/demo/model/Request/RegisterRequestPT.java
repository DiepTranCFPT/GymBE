package com.example.demo.model.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestPT {
    String name;
    String password;
    String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email;

    String speciliation;
    int experienceYear;
}
