package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "external_id", "description", "items", "amount", "currency", "amount"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class XenditInvoiceDto extends XenditBaseInvoiceDto {

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("invoice_url")
    private String invoiceUrl;

    @JsonProperty("success_redirect_url")
    private String successRedirectUrl;

    @JsonProperty("failure_redirect_url")
    private String failureRedirectUrl;

    private List<ItemsDto> items;

}
