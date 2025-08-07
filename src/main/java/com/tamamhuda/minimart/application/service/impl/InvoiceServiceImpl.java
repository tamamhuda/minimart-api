package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoiceDto;
import com.tamamhuda.minimart.application.mapper.InvoiceCustomerMapper;
import com.tamamhuda.minimart.application.mapper.InvoiceMapper;
import com.tamamhuda.minimart.application.service.InvoiceService;
import com.tamamhuda.minimart.domain.entity.Invoice;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.InvoiceStatus;
import com.tamamhuda.minimart.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final XenditServiceImpl xenditService;
    private final InvoiceCustomerMapper invoiceCustomerMapper;


    @Override
    public Invoice buildInvoice(User user, InvoiceRequestDto request) {
        CustomerDto customer = invoiceCustomerMapper.toDto(xenditService.createCustomer(user));

        XenditInvoiceDto xenditInvoice = xenditService.createXenditInvoice(customer, request);

        InvoiceDto invoiceDto = InvoiceDto.builder()
                .customerName(customer.getGivenNames())
                .customerEmail(customer.getEmail())
                .expiryDate(xenditInvoice.getExpiryDate())
                .externalId(xenditInvoice.getExternalId())
                .invoiceUrl(xenditInvoice.getInvoiceUrl())
                .totalAmount(new BigDecimal(xenditInvoice.getAmount().toString()))
                .status(InvoiceStatus.UNPAID.name())
                .issuedDate(Instant.now())
                .build();

        return invoiceMapper.toEntity(invoiceDto);
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
