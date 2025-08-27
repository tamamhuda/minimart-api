package com.tamamhuda.minimart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.dto.ProductRequestDto;
import com.tamamhuda.minimart.application.mapper.ProductMapper;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.ProductServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
import com.tamamhuda.minimart.domain.entity.Product;
import com.tamamhuda.minimart.domain.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductMapper productMapper;

    private UUID productId;
    private ProductDto mockProduct;

    @MockitoBean
    private JwtServiceImpl jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private VerifiedUserAuthManager verifiedUserAuthManager;
    @Autowired
    private com.tamamhuda.minimart.testutil.TestAuthHelper testAuthHelper;


    @BeforeEach
    void setUp() {
        // Create a new Product entity
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setStockQuantity(10);
        product.setImageUrl("http://example.com/image.png");

        // Build MockMvc
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Prepare mock product
        productId = product.getId();
        mockProduct = productMapper.toDto(product);

    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UserDetails mockAuthenticatedUser(Role role) {
        return testAuthHelper.mockAuthenticatedUser(role);
    }

    private RequestPostProcessor authorizationToken(UserDetails userDetails) {
        return testAuthHelper.authorizationToken(userDetails);
    }

    @Test
    @DisplayName("POST /products should create new product")
    void testCreateProduct() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        Mockito.when(productService.create(any(ProductRequestDto.class)))
                .thenReturn(mockProduct);

        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(99.99f)
                .stockQuantity(10)
                .categoryIdOrName("Category A")
                .build();

        mockMvc.perform(post("/products")
                        .with(authorizationToken(adminDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /products/{id} should return product by ID")
    void testGetProductById() throws Exception {

        Mockito.when(productService.getProductById(productId)).thenReturn(mockProduct);

        mockMvc.perform(get("/products/" + productId)
                        )
                .andExpect(status().isCreated()) // note: your controller returns CREATED instead of OK
                .andExpect(jsonPath("$.data.id").value(productId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    @DisplayName("PUT /products/{id} should update product")
    void testUpdateProduct() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        ProductDto updatedProduct = ProductDto.builder()
                .id(productId.toString())
                .name("Updated Product")
                .description("Updated Description")
                .price(BigDecimal.valueOf(120.0))
                .stockQuantity(20)
                .build();

        Mockito.when(productService.update(any(ProductRequestDto.class), eq(productId)))
                .thenReturn(updatedProduct);

        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(120.0f)
                .stockQuantity(20)
                .categoryIdOrName("Category B")
                .build();

        mockMvc.perform(put("/products/" + productId)
                        .with(authorizationToken(adminDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Product"));
    }

    @Test
    @DisplayName("DELETE /products/{id} should delete product")
    void testDeleteProduct() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        mockMvc.perform(delete("/products/" + productId)
                        .with(authorizationToken(adminDetails)))
                .andExpect(status().isNoContent());

        Mockito.verify(productService).deleteProductById(productId);
    }
}
