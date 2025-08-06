package com.tamamhuda.minimart.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tamamhuda.minimart.domain.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table
@Getter
@Setter
public class Invoice extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @Column(name = "invoice_pdf")
    private String invoicePdf;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "issued_date")
    private Instant issuedDate;

}
