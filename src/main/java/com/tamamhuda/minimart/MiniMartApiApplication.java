package com.tamamhuda.minimart;

import com.tamamhuda.minimart.common.dto.TestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@SpringBootApplication
@EntityScan("com.tamamhuda.minimart.domain.entity")
@Validated
public class MiniMartApiApplication {


	@GetMapping("/healthz")
	public ResponseEntity<Map<String, Object>> healthz() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "OK");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/test/{room}")
	public ResponseEntity<?> test(@RequestBody @Valid TestDto request,
								  @PathVariable("room") @Positive(message = "room should positive") Integer room,
								  @RequestParam("filter") @NotBlank(message = "filter query is required") String filter) {
		return ResponseEntity.status(HttpStatus.OK).body(request);
	}

	public static void main(String[] args) {
		SpringApplication.run(MiniMartApiApplication.class, args);
	}



}
