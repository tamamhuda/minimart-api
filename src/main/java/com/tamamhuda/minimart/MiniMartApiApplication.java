package com.tamamhuda.minimart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@Validated
@SpringBootApplication
@Tag(
        name = "MiniMart Api"
)
public class MiniMartApiApplication {


	@GetMapping("/healthz")
    @Operation(
            summary = "Api health check",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    "status" : "200",
                                                    "message" : "OK"
                                                    }
                                                    """
                                    )

                            )
                    ),
            }
    )
	public ResponseEntity<Map<String, Object>> healthz() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "OK");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	public static void main(String[] args) {
		SpringApplication.run(MiniMartApiApplication.class, args);
	}



}
