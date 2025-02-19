package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer,String> {

    Trainer findByUser(User user);

}
