package com.tamamhuda.minimart;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.PaymentServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
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
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private PaymentServiceImpl paymentService;

    @MockitoBean
    private JwtServiceImpl jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private VerifiedUserAuthManager verifiedUserAuthManager;

    @Autowired
    private TestAuthHelper testAuthHelper;

    private UUID paymentId;
    private UUID orderId;
    private PaymentDto mockPayment;

    private InvoiceDto mockInvoice;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Mock Payment and Invoice
        paymentId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        mockInvoice = InvoiceDto.builder()
                .id(UUID.randomUUID().toString())
                .externalId("INV-001")
                .customerName("John Doe")
                .customerEmail("john.doe@example.com")
                .totalAmount(BigDecimal.valueOf(100))
                .status("PENDING")
                .invoicePdf("base64pdfcontent")
                .invoiceUrl("https://example.com/invoice/INV-001")
                .issuedDate(Instant.now())
                .expiryDate(Instant.now().plusSeconds(86400).toString())
                .build();

        mockPayment = PaymentDto.builder()
                .id(paymentId.toString())
                .totalAmount(BigDecimal.valueOf(100))
                .paymentMethod("CREDIT_CARD")
                .status("PENDING")
                .paidAt(Instant.now())
                .invoice(mockInvoice)
                .build();
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
    @DisplayName("POST /orders/{order_id}/payments should start payment for an order with invoice")
    void testStartPaymentForOrder() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(paymentService.startPaymentForOrder(any(), eq(orderId)))
                .thenReturn(mockPayment);

        mockMvc.perform(post("/orders/" + orderId + "/payments")
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(paymentId.toString()))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.payment_method").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.data.total_amount").value(100))
                .andExpect(jsonPath("$.data.invoice.id").value(mockInvoice.getId()))
                .andExpect(jsonPath("$.data.invoice.external_id").value("INV-001"))
                .andExpect(jsonPath("$.data.invoice.customer_name").value("John Doe"))
                .andExpect(jsonPath("$.data.invoice.invoice_url").value("https://example.com/invoice/INV-001"));
    }

    @Test
    @DisplayName("GET /orders/{order_id}/payments/{payment_id} should return payment details with invoice")
    void testGetPaymentDetails() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(paymentService.getPaymentDetails(eq(paymentId), eq(orderId)))
                .thenReturn(mockPayment);

        mockMvc.perform(get("/orders/" + orderId + "/payments/" + paymentId)
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(paymentId.toString()))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.payment_method").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.data.total_amount").value(100))
                .andExpect(jsonPath("$.data.invoice.id").value(mockInvoice.getId()))
                .andExpect(jsonPath("$.data.invoice.external_id").value("INV-001"))
                .andExpect(jsonPath("$.data.invoice.customer_name").value("John Doe"))
                .andExpect(jsonPath("$.data.invoice.invoice_url").value("https://example.com/invoice/INV-001"));
    }
}


