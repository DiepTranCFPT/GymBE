package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AuthenticationRepository extends JpaRepository<User, String> {   // dua ra daatabase
    User findByEmailAndRole(String email, String role);


//    User findByEmail(String email);

    Optional<User> findByPhone(String numer);

    Optional<User> findByEmailAndAndDeletedIsFalse(String eString);


//    boolean existsByEmailOrPhone(String email, String phone);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);


    Optional<User> findById(String id);

    Optional<User> findByFirebaseUid(String firebase);

    List<User> findByAvataIsNotNull();

}
