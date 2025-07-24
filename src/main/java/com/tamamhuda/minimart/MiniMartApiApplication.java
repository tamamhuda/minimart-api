package com.tamamhuda.minimart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@SpringBootApplication
public class MiniMartApiApplication {


	@GetMapping("/healthz")
	public ResponseEntity<Map<String, Object>> healthz() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "OK");
		return ResponseEntity.ok(response);
	}

	public static void main(String[] args) {
		SpringApplication.run(MiniMartApiApplication.class, args);
	}

}
