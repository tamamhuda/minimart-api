package com.tamamhuda.minimart.application.dto.xendit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XenditInvoicePayloadDto extends XenditBaseInvoiceDto{

    @JsonProperty("paid_at")
    private String paidAt;

    @JsonProperty("paid_amount")
    private Number paidAmount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_channel")
    private String paymentChannel;

    @JsonProperty("payment_details")
    private PaymentDetailsDto paymentDetails;

    @JsonProperty("payment_method_id")
    private String paymentMethodId;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @SuperBuilder
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentDetailsDto {
        private String source;
    }
}
