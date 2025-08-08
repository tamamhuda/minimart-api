package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Optional<Invoice> findByExternalId(String externalId);
}
