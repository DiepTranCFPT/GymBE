package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.Subscriptions;
import jdk.jshell.JShell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptonRepository extends JpaRepository<Subscriptions, String> {
    Subscriptions findByMembers(Members members);

}
