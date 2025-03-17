package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,String> {

    Optional<Trainer> findByUser(User user);


    boolean existsByUser(User user);

    Optional<Trainer> findById(String id);

    boolean existsByUser_Id(String userId);

    @Query("SELECT t FROM Trainer t WHERE t.user.deleted = false AND t.locked = false")
    List<Trainer> findAllActive();

}
