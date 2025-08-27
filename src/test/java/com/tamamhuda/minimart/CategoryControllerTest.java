package com.tamamhuda.minimart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.CategoryDto;
import com.tamamhuda.minimart.application.dto.CategoryRequestDto;
import com.tamamhuda.minimart.application.mapper.CategoryMapper;
import com.tamamhuda.minimart.application.service.impl.CategoryServiceImpl;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.domain.entity.Category;
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
class CategoryControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private CategoryServiceImpl categoryService;

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

    private UUID categoryId;
    private CategoryDto mockCategory;

    private List<CategoryDto> mockCategoryList;

    @Autowired
    private CategoryMapper categoryMapper; // make sure you have this mapper

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Create Category entity
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Test Category 1");
        category.setDescription("Category Description");

        Category category2 = new Category();
        category2.setId(UUID.randomUUID());
        category2.setName("Test Category 2");
        category2.setDescription("Category Description 2");

        // Convert to DTO using mapper
        mockCategory = categoryMapper.toDto(category);

        // Keep ID for tests
        categoryId = UUID.fromString(mockCategory.getId());

        mockCategoryList = List.of(mockCategory, categoryMapper.toDto(category2));

    }
    @AfterEach
    void tearDown() {
        // Clear SecurityContext
        SecurityContextHolder.clearContext();
    }

    private UserDetails mockAuthenticatedUser(Role role) {
        return testAuthHelper.mockAuthenticatedUser(role);
    }

    private RequestPostProcessor authorizationToken(UserDetails userDetails) {
        return testAuthHelper.authorizationToken(userDetails);
    }

    @Test
    @DisplayName("POST /categories should create new category")
    void testCreateCategory() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        Mockito.when(categoryService.create(any(CategoryRequestDto.class)))
                .thenReturn(mockCategory);

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Category Description");

        mockMvc.perform(post("/categories")
                        .with(authorizationToken(adminDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Category 1"));
    }

    @Test
    @DisplayName("PUT /categories/{id} should update category")
    void testUpdateCategory() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        Category updatedCategory = categoryMapper.toEntity(mockCategory);
        updatedCategory.setName("Updated Category");
        updatedCategory.setDescription("Updated Description");

        Mockito.when(categoryService.update(any(CategoryRequestDto.class), eq(categoryId)))
                .thenReturn(categoryMapper.toDto(updatedCategory));

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Category");
        requestDto.setDescription("Updated Description");

        mockMvc.perform(put("/categories/" + categoryId)
                        .with(authorizationToken(adminDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Category"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"));
    }

    @Test
    @DisplayName("DELETE /categories/{id} should delete category")
    void testDeleteCategory() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        mockMvc.perform(delete("/categories/" + categoryId)
                        .with(authorizationToken(adminDetails)))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService).delete(categoryId);
    }

    @Test
    @DisplayName("GET /categories should return paginated categories")
    void testGetAllCategories() throws Exception {
        // Create a mock PageDto
        PageDto<CategoryDto> mockPage = PageDto.<CategoryDto>builder()
                .content(mockCategoryList)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(2)
                .totalPages(1)
                .last(true)
                .build();

        // Mock service call
        Mockito.when(categoryService.getAllCategories(any(Pageable.class)))
                .thenReturn(mockPage);

        // Perform GET request
        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].name").value("Test Category 1"))
                .andExpect(jsonPath("$.data.content[1].name").value("Test Category 2"))
                .andExpect(jsonPath("$.data.page_number").value(0))
                .andExpect(jsonPath("$.data.page_size").value(10))
                .andExpect(jsonPath("$.data.total_elements").value(2))
                .andExpect(jsonPath("$.data.total_pages").value(1))
                .andExpect(jsonPath("$.data.last").value(true));
    }
}
