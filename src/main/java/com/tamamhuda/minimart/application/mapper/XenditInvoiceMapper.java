package com.tamamhuda.minimart.application.mapper;

import com.tamamhuda.minimart.application.dto.xendit.CustomerDto;
import com.tamamhuda.minimart.application.dto.xendit.ItemsDto;
import com.tamamhuda.minimart.application.dto.xendit.XenditInvoiceDto;
import com.tamamhuda.minimart.domain.entity.User;
import com.xendit.model.Invoice;
import com.xendit.model.ItemInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface XenditInvoiceMapper extends GenericDtoMapper<XenditInvoiceDto, Invoice> {

    @Override
    @Mapping(source = "items", target = "items")
//    @Mapping(source = "availableBanks", target = "availableBanks")
    XenditInvoiceDto toDto(Invoice entity);

    ItemInvoice toEntity(ItemsDto dto);

    ItemsDto toDto(ItemInvoice entity);

    @Override
    @Mapping(source = "items", target = "items")
//    @Mapping(source = "availableBanks", target = "availableBanks")
    Invoice toEntity(XenditInvoiceDto dto);

    CustomerDto userToCustomerInfoDto(User user);

}
