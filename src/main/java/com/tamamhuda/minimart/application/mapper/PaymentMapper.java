package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.domain.entity.Payment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper extends GenericDtoMapper<PaymentDto, Payment> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(PaymentDto paymentDto, @MappingTarget Payment payment);
}
