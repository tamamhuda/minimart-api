package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.xendit.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceCustomerMapper extends GenericDtoMapper<CustomerDto, Customer> {

}
