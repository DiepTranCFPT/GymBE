package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.MemberShipPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipPlansRepository extends JpaRepository<MemberShipPlans,String> {
    MemberShipPlans findByName(String name);
    Boolean existsByName(String name);
    List<MemberShipPlans> findAll();

}
