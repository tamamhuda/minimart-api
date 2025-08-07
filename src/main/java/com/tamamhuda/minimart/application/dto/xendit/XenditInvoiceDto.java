package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tamamhuda.minimart.application.dto.BaseDto;
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
public class XenditInvoiceDto extends BaseDto {

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("user_id")
    private String userid;

    private String status;

    @JsonProperty("merchant_name")
    private String merchantName;

    @JsonProperty("merchant_profile_picture_url")
    private String merchantProfilePictureUrl;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("invoice_url")
    private String invoiceUrl;

    private Number amount;

    private String description;

    @JsonProperty("paid_at")
    private String paidAt;

    @JsonProperty("paid_amount")
    private Number paidAmount;

    private CustomerDto customer;

    @JsonProperty("success_redirect_url")
    private String successRedirectUrl;

    @JsonProperty("failure_redirect_url")
    private String failureRedirectUrl;

    private List<ItemsDto> items;

    @JsonProperty("available_banks")
    private List<AvailableBankDto> availableBanks;

    private String currency;

}
