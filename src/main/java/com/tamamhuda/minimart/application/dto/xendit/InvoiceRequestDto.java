package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString
public class InvoiceRequestDto {

    @JsonProperty("external_id")
    private String externalId;

    private BigDecimal amount;

    private String description;

    @JsonProperty("invoice_duration")
    private Integer invoiceDuration;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private CustomerDto customer;

    @JsonProperty("success_redirect_url")
    private String successRedirectUrl;

    @JsonProperty("failure_redirect_url")
    private String failureRedirectUrl;

    private List<ItemsDto> items;

    private String currency;


}
