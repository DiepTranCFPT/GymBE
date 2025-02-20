package com.gymsystem.cyber.model.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginGoogleRequest {
    //    long locationId;
    String name;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email;
}
