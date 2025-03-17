package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews,String> {
}
