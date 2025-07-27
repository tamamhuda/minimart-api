package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
