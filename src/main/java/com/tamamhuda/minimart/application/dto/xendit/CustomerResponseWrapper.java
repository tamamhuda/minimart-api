package com.tamamhuda.minimart.application.dto.xendit;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
public class CustomerResponseWrapper {
    private List<Map<String, Object>> data;
}
