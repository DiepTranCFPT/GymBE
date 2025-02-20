package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    // dua ra daatabase
    //    User findByEmail(String email);
}
