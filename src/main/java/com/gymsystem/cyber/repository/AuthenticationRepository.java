package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<User, String> {   // dua ra daatabase
    User findByEmailAndRole(String email, String role);

//    User findByEmail(String email);

    Optional<User> findByPhone(String numer);

    Optional<User> findByEmailAndAndDeletedIsFalse(String eString);

//    boolean existsByEmailOrPhone(String email, String phone);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User findAllByEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email);

    Optional<User> findByName(String name);

    Optional<User> findById(String id);

    Optional<User> findByFirebaseUid(String firebase);

    List<User> findAllByAvataIsNotNull();

//    List<User> findAllByAvataIsNotNullAndMembers_ExpireDate_DayOfMonth

}
