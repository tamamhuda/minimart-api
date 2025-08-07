package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoiceDto;
import com.tamamhuda.minimart.domain.entity.User;
import com.xendit.model.Customer;

import java.util.Optional;

public interface XenditService {

    Optional<Customer> getCustomerByReferenceId(String referenceId);

    Customer createCustomer(User user);

    XenditInvoiceDto createXenditInvoice(CustomerDto customer, InvoiceRequestDto request);

}
