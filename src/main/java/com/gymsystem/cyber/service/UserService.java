package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.AccountDetails;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final AuthenticationRepository authenticationRepository;

    @Autowired
    public UserService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (AccountUtils.isValidEmail(username)) {
            user = authenticationRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("email is not a valid!"));
        } else if (AccountUtils.isValidPhoneNumber(username)) {
            user = authenticationRepository.findByPhone(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone number is not a valid!"));
        } else {
            throw new UsernameNotFoundException("Account does not exist!");
        }
        return new AccountDetails(user);
    }
}
