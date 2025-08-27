package com.tamamhuda.minimart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.CartDto;
import com.tamamhuda.minimart.application.dto.CartItemDto;
import com.tamamhuda.minimart.application.dto.CartItemRequestDto;
import com.tamamhuda.minimart.application.dto.ProductDto;
import com.tamamhuda.minimart.application.service.impl.CartServiceImpl;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private CartServiceImpl cartService;

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

    private UUID cartItemId;
    private UUID cartId;
    private CartItemDto mockCartItem;

    private UUID productId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Prepare mock product and cart item
        productId = UUID.randomUUID();
        cartItemId = UUID.randomUUID();

        ProductDto product = ProductDto.builder()
                .id(productId.toString())
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .stockQuantity(10)
                .build();

        cartId = UUID.randomUUID();
        mockCartItem = CartItemDto.builder()
                .id(cartItemId.toString())
                .product(product)
                .quantity(2)
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
    @DisplayName("POST /cart/items should add item to cart")
    void testAddCartItem() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(cartService.addCartItem(any(), any(CartItemRequestDto.class)))
                .thenReturn(mockCartItem);

        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .productId(productId.toString())
                .quantity(2)
                .build();

        mockMvc.perform(post("/cart/items")
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(cartItemId.toString()))
                .andExpect(jsonPath("$.data.product.id").value(productId.toString()))
                .andExpect(jsonPath("$.data.quantity").value(2));
    }

    @Test
    @DisplayName("GET /cart/items/{item_id} should return cart item")
    void testGetCartItem() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(cartService.getCartItem(cartItemId))
                .thenReturn(mockCartItem);

        mockMvc.perform(get("/cart/items/" + cartItemId)
                        .with(authorizationToken(customerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(cartItemId.toString()))
                .andExpect(jsonPath("$.data.product.id").value(productId.toString()))
                .andExpect(jsonPath("$.data.quantity").value(2));
    }

    @Test
    @DisplayName("PUT /cart/items/{item_id} should update cart item")
    void testUpdateCartItem() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        CartItemDto updatedCartItem = CartItemDto.builder()
                .id(cartItemId.toString())
                .product(mockCartItem.getProduct())
                .quantity(5)
                .build();

        Mockito.when(cartService.updateCartItem(eq(cartItemId), any(CartItemRequestDto.class)))
                .thenReturn(updatedCartItem);

        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .productId(productId.toString())
                .quantity(5)
                .build();

        mockMvc.perform(put("/cart/items/" + cartItemId)
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(cartItemId.toString()))
                .andExpect(jsonPath("$.data.quantity").value(5));
    }

    @Test
    @DisplayName("DELETE /cart/items/{item_id} should remove cart item")
    void testRemoveCartItem() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        mockMvc.perform(delete("/cart/items/" + cartItemId)
                        .with(authorizationToken(customerDetails)))
                .andExpect(status().isNoContent());

        Mockito.verify(cartService).removeCartItem(any(), eq(cartItemId));
    }

    @Test
    @DisplayName("GET /cart should return user cart")
    void testGetCart() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);
        PageDto<CartItemDto> mockPage = PageDto.<CartItemDto>builder()
                .content(List.of(mockCartItem))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();

        CartDto mockCart = CartDto.builder()
                .id(cartId.toString())
                .userId(UUID.randomUUID())
                .cartItems(mockPage)
                .build();

        Mockito.when(cartService.getCart(any(), any(Pageable.class)))
                .thenReturn(mockCart);

        mockMvc.perform(get("/cart")
                        .with(authorizationToken(customerDetails))
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(cartId.toString()))
                .andExpect(jsonPath("$.data.cart_items.content.length()").value(1))
                .andExpect(jsonPath("$.data.cart_items.page_number").value(0))
                .andExpect(jsonPath("$.data.cart_items.page_size").value(10))
                .andExpect(jsonPath("$.data.cart_items.total_elements").value(1))
                .andExpect(jsonPath("$.data.cart_items.total_pages").value(1))
                .andExpect(jsonPath("$.data.cart_items.last").value(true));
    }
}

