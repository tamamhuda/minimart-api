package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.xendit.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceCustomerMapper extends GenericDtoMapper<CustomerDto, Customer> {

}
