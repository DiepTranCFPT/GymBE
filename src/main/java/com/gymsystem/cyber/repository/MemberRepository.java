package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members,String> {
    Optional<Members> findByUser_Id(String userId);

    Optional<Members> findByUser(User user);
}
