package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.enums.UserRole;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AuthenticationRepository authenticationRepository;

    public void createAdmin(){
        User newUser = new User();
        newUser.setName("admin");
        newUser.setPassword("admin");
        newUser.setEnable(true);
        newUser.setEmail("admin@gmail.com");
        newUser.setRole(UserRole.ADMIN);
        authenticationRepository.save(newUser);
    }
}
