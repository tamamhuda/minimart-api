package com.tamamhuda.minimart.config;


import com.tamamhuda.minimart.domain.entity.Category;
import com.tamamhuda.minimart.domain.entity.Product;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.Role;
import com.tamamhuda.minimart.domain.repository.CategoryRepository;
import com.tamamhuda.minimart.domain.repository.ProductRepository;
import com.tamamhuda.minimart.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"dev", "local"})
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        try {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setFullName("Admin");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("123456");
                admin.setRoles(Role.ADMIN);

                User user = new User();
                user.setFullName("John Doe");
                user.setUsername("johndoe");
                user.setEmail("johndoe@example.com");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRoles(Role.CUSTOMER);

                userRepository.saveAll(List.of(admin,user));
            }

            if (productRepository.count() == 0) {
                Category category = new Category();
                category.setName("category1");
                category.setDescription("Category 1");
                categoryRepository.save(category);

                Product p1 = new Product();
                p1.setName("Product 1");
                p1.setDescription("Description 1");
                p1.setStockQuantity(5);
                p1.setPrice(BigDecimal.valueOf(25000));
                p1.setCategory(category);

                productRepository.saveAll(List.of(p1));

                log.info("Product 1 created {}", p1.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
