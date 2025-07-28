package com.tamamhuda.minimart.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tamamhuda.minimart.domain.enums.PaymentMethod;
import com.tamamhuda.minimart.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Payment {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", nullable = false, updatable = false)
    private String id =  UUID.randomUUID().toString();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    private Instant paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus Status = PaymentStatus.PENDING;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",nullable = false,  updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Instant updatedAt;
}
