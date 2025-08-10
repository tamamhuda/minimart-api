package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"content", "page_number", "page_size", "total_elements", "total_pages", "last"})
public class PageResponse<T> {
    private List<T> content;

    @JsonProperty("page_number")
    private int pageNumber;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("page_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;

    private boolean last;
}
