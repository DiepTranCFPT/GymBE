package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Reviews;
import com.gymsystem.cyber.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews,String> {

    List<Reviews> findByTrainerId(String id);

}
