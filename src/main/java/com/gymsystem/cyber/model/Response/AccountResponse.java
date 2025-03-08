package com.gymsystem.cyber.model.Response;

import com.gymsystem.cyber.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull
public class AccountResponse extends User {
    private String token;
}
