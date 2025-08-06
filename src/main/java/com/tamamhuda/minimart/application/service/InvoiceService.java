package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.domain.entity.Invoice;

import java.util.UUID;

public interface InvoiceService {

    Invoice create(InvoiceDto invoice);

    Invoice getById(UUID invoiceId);

    Invoice updateInvoice(InvoiceDto invoiceDto,  UUID invoiceId);
}
