package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.mapper.InvoiceMapper;
import com.tamamhuda.minimart.application.service.InvoiceService;
import com.tamamhuda.minimart.domain.entity.Invoice;
import com.tamamhuda.minimart.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public Invoice create(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found")
        );
    }

    @Override
    public Invoice updateInvoice(InvoiceDto invoiceDto, UUID invoiceId) {
        Invoice existingInvoice = getById(invoiceId);
        invoiceMapper.updateFromDto(invoiceDto, existingInvoice);

        return invoiceRepository.save(existingInvoice);
    }
}
