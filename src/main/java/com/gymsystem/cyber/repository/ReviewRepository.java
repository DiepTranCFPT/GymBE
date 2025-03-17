package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Reviews;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews,String> {
    @Override
    Optional<Reviews> findById(String s);

    List<Reviews> findByTrainer_Id(String id);
}
