package com.example.demo.repository;

import com.example.demo.entity.User;

import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<User, Long>
{   // dua ra daatabase
    User findByEmailAndRole(String email,String role);

    User findByEmail(String email);

    User findByPhone(String numer);

}
