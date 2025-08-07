package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.domain.entity.Invoice;
import com.tamamhuda.minimart.domain.entity.User;

import java.util.UUID;

public interface InvoiceService {

    Invoice buildInvoice(User user, InvoiceRequestDto request);

    Invoice getById(UUID invoiceId);

    Invoice updateInvoice(InvoiceDto invoiceDto, UUID invoiceId);

}
