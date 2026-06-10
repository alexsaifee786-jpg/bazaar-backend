package com.bazaar.backend;

import com.bazaar.backend.model.Category;
import com.bazaar.backend.model.Product;
import com.bazaar.backend.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CategoryRepository categoryRepository) {
        return args -> {
            // 1. Pehle Parent Category banayein
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Mobile, Laptops and gadgets")
                    .products(new ArrayList<>()) // Khali list initialize ki
                    .build();

            // 2. Child Products banayein aur unhe Category assign karein (Owning side map kiya)
            Product p1 = Product.builder()
                    .name("iPhone 15 Pro")
                    .description("Apple flagship phone")
                    .price(new BigDecimal("129999.00"))
                    .stock(50)
                    .category(electronics) // Product ko bataya uski category kaun si hai
                    .build();

            Product p2 = Product.builder()
                    .name("MacBook Air M3")
                    .description("Apple lightweight laptop")
                    .price(new BigDecimal("114999.00"))
                    .stock(30)
                    .category(electronics) // Product ko bataya uski category kaun si hai
                    .build();

            // 3. Category ki list me bhi dono products ko add kar diya (Bidirectional sync)
            electronics.getProducts().add(p1);
            electronics.getProducts().add(p2);

            // 4. Sirf Parent ko save karenge, CascadeType.ALL ki wajah se Products khud save ho jayenge!
            categoryRepository.save(electronics);

            System.out.println("🚀 SUCCESS: Category and Cascade Products Saved in MySQL Successfully!");
        };
    }
}
