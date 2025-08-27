package com.tamamhuda.minimart;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.OrderServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.enums.Role;
import com.tamamhuda.minimart.testutil.TestAuthHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private OrderServiceImpl orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtServiceImpl jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private VerifiedUserAuthManager verifiedUserAuthManager;

    @Autowired
    private TestAuthHelper testAuthHelper;

    private UUID orderId;
    private OrderDto mockOrder;
    private List<OrderDto> mockOrderList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Mock Order entity
        orderId = UUID.randomUUID();

        UserSummaryDto userSummary = UserSummaryDto.builder()
                .id(UUID.randomUUID())
                .build();

        OrderItemDto orderItem = OrderItemDto.builder()
                .product(ProductDto.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Test Product")
                        .price(BigDecimal.valueOf(50))
                        .build())
                .quantity(2)
                .build();

        mockOrder = OrderDto.builder()
                .id(orderId.toString())
                .user(userSummary)
                .orderItems(List.of(orderItem))
                .status("PENDING")
                .totalPrice(BigDecimal.valueOf(100))
                .build();

        mockOrderList = List.of(mockOrder);
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
    @DisplayName("POST /orders/checkout should create a new order")
    void testCheckout() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        OrderRequestDto requestDto = OrderRequestDto.builder()
                .cartItemIds(List.of(UUID.randomUUID()))
                .build();

        Mockito.when(orderService.checkout(any(), any(OrderRequestDto.class)))
                .thenReturn(mockOrder);

        mockMvc.perform(post("/orders/checkout")
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(orderId.toString()))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.total_price").value(100));
    }

    @Test
    @DisplayName("GET /orders should return paginated orders")
    void testGetAllUserOrders() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        PageDto<OrderDto> mockPage = PageDto.<OrderDto>builder()
                .content(mockOrderList)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();

        Mockito.when(orderService.getAllUserOrders(any(), any(Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/orders")
                        .with(authorizationToken(customerDetails))
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(orderId.toString()))
                .andExpect(jsonPath("$.data.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.data.page_number").value(0))
                .andExpect(jsonPath("$.data.page_size").value(10))
                .andExpect(jsonPath("$.data.total_elements").value(1))
                .andExpect(jsonPath("$.data.total_pages").value(1))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("GET /orders/{order_id} should return order details")
    void testGetOrderById() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(mockOrder);

        mockMvc.perform(get("/orders/" + orderId)
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(orderId.toString()))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.total_price").value(100));
    }
}