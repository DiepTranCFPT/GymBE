package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.MemberShipPlans;
import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.Subscriptions;
import io.grpc.internal.ObjectPool;
import jdk.jshell.JShell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptonRepository extends JpaRepository<Subscriptions, String> {
    Subscriptions findByMembers(Members members);
    boolean existsByMembersAndMemberShipPlans(Members members, MemberShipPlans memberShipPlans);

    Optional<Subscriptions> findByMembers_Id(String membersId);
}
