package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
