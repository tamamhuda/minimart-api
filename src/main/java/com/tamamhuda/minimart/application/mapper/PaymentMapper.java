package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.domain.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper extends GenericDtoMapper<PaymentDto, Payment> {

    @Override
    @Mapping(source = "invoice", target = "invoice")
    PaymentDto toDto(Payment entity);
}
