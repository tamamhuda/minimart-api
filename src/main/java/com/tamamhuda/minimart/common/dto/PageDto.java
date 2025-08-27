package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@JsonInclude(JsonInclude.Include.ALWAYS)
@Schema(name = "Page")
@JsonPropertyOrder({"content", "page_number", "page_size", "total_elements", "total_pages", "last"})
public class PageDto<T> {
    private List<T> content;

    @JsonProperty("page_number")
    private int pageNumber;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;

    private boolean last;
}
