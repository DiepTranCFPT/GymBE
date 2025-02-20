package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.MemberShipPlans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipPlansRepository extends JpaRepository<MemberShipPlans,String> {
    Optional<MemberShipPlans> findByName(String name);
    Boolean existsByName(String name);
}
