package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XenditBaseInvoiceDto {

    private String id;

    @JsonProperty("external_id")
    private String externalId;

    private String status;

    @JsonProperty("user_id")
    private String userid;

    @JsonProperty("merchant_name")
    private String merchantName;

    private Number amount;

    private String currency;

    @JsonProperty("is_high")
    private boolean isHigh;

    private String description;
}
