package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer,String> {

    Optional<Trainer> findByUser(User user);


    boolean existsByUser(User user);

    Optional<Trainer> findById(String id);

}
