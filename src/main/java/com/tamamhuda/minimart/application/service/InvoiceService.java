package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoicePayloadDto;
import com.tamamhuda.minimart.domain.entity.Invoice;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface InvoiceService {

    Invoice buildInvoice(User user, InvoiceRequestDto request);

    Invoice getById(UUID invoiceId);

    Invoice updateInvoice(InvoiceDto invoiceDto, UUID invoiceId);

    ResponseEntity<String> webhookInvoiceHandler(XenditInvoicePayloadDto payload);
}
