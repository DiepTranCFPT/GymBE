package com.gymsystem.cyber.model.Response;

import com.gymsystem.cyber.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse extends User {
    private String token;
}
