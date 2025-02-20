package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<User, Long> {   // dua ra daatabase

//    User findByEmail(String email);

    Optional<User> findByPhone(String numer);

    Optional<User> findByEmailAndAndDeletedIsFalse(String eString);


//    boolean existsByEmailOrPhone(String email, String phone);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

}
