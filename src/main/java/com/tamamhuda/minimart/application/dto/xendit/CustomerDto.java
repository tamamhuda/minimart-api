package com.tamamhuda.minimart.application.dto.xendit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor@Getter
@NoArgsConstructor
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("given_names")
    private String givenNames;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String type;

    private String email;

    @JsonProperty("individual_detail")
    private IndividualDetail individualDetail;

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndividualDetail {
        @JsonProperty("given_names")
        private String givenNames;
    }

}
