package com.tamamhuda.minimart.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.tamamhuda.minimart.application.dto.xendit.InvoiceRequestDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoiceDto;
import com.tamamhuda.minimart.application.mapper.InvoiceCustomerMapper;
import com.tamamhuda.minimart.application.mapper.XenditInvoiceMapper;
import com.tamamhuda.minimart.application.service.XenditService;
import com.tamamhuda.minimart.application.dto.xendit.CustomerResponseWrapper;
import com.tamamhuda.minimart.domain.entity.User;
import com.xendit.Xendit;
import com.xendit.Xendit.Option;
import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Customer;
import com.xendit.model.Invoice;
import com.xendit.network.NetworkClient;
import com.xendit.network.RequestResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class XenditServiceImpl implements XenditService {

    private final XenditInvoiceMapper xenditInvoiceMapper;
    private final ObjectMapper objectMapper;
    private final XenditClient xenditClient;
    private final InvoiceCustomerMapper invoiceCustomerMapper;
    private final NetworkClient requestClient = Xendit.getRequestClient();

    private CustomerResponseWrapper requestGetCustomerByReferenceId(String referenceId) throws XenditException {
        Option opt = xenditClient.customer.getOpt();
        String url = String.format("%s/customers?reference_id=%s", opt.getXenditURL(), referenceId);

        return this.requestClient.request(
                RequestResource.Method.GET,
                url,
                null,
                opt.getApiKey(),
                CustomerResponseWrapper.class
        );
    }

    @Override
    public Optional<Customer> getCustomerByReferenceId(String referenceId) {
        try {
            CustomerResponseWrapper response = requestGetCustomerByReferenceId(referenceId);

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                return Optional.empty();
            }

            Map<String, Object> customerMap = response.getData().getFirst();

            CustomerDto customerDto = objectMapper.convertValue(customerMap, CustomerDto.class);

            Customer customer = invoiceCustomerMapper.toEntity(customerDto);

            String prettyCustomerJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(customer);

            log.info("Customer by reference id:\n{}", prettyCustomerJson);

            customer.setGivenNames(customerDto.getIndividualDetail().getGivenNames());

            return Optional.of(customer);
        } catch (Exception e) {
            log.error("Failed to get customer by reference ID", e);
            return Optional.empty();
        }
    }

    @Override
    public Customer createCustomer(User user) {
        try {

            String referenceId = "customer-"+user.getId().toString();
            Optional<Customer> existingCustomer = getCustomerByReferenceId(referenceId);


            if(existingCustomer.isPresent()) {
                return existingCustomer.get();
            }

            CustomerDto request = xenditInvoiceMapper.userToCustomerInfoDto(user);
            request.setReferenceId(referenceId);
            request.setMobileNumber("+628950145579");
            request.setPhoneNumber("+628950145579");
            request.setType("INDIVIDUAL");

            request.setIndividualDetail(CustomerDto.IndividualDetail.builder()
                    .givenNames(user.getFullName()).build());

            Map<String, Object> params = objectMapper.convertValue(request, new TypeReference<>() {});

            log.info("Creating customer with parameters: {}", params.toString());

            Customer customer = xenditClient.customer.createCustomer(params);

            customer.setGivenNames(request.getIndividualDetail().getGivenNames());

            return customer;

        } catch (XenditException e) {
            log.error("Error while creating customer");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public XenditInvoiceDto createXenditInvoice(CustomerDto customer, InvoiceRequestDto request) {
        try {
            request.setCustomer(customer);

            Map<String, Object> params = objectMapper.convertValue(request, new TypeReference<>() {});

            log.info("Requesting Xendit invoice {}", params.toString());

            Invoice invoice = xenditClient.invoice.create(params);

            return xenditInvoiceMapper.toDto(invoice);

        } catch (XenditException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
