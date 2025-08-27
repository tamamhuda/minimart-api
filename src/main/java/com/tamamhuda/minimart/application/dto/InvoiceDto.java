package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "InvoiceSchema")
@JsonPropertyOrder({"id", "external_id", "customer_name", "customer_email", "total_amount", "status", "invoice_pdf", "issued_date", "invoice_url", "expiry_date"})
public class InvoiceDto extends BaseDto {


    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("invoice_url")
    private String invoiceUrl;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("expiry_date")
    private String expiryDate;

    private String status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("invoice_pdf")
    private String invoicePdf;

    @JsonProperty("issued_date")
    private Instant issuedDate;

}
