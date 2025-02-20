package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Members,String> {

}
