package com.bazaar.backend;

import com.bazaar.backend.model.Category;
import com.bazaar.backend.model.Product;
import com.bazaar.backend.repository.CategoryRepository;
import com.bazaar.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            // 1. Database cleanup taaki fresh data insert ho sake
            productRepository.deleteAll();
            categoryRepository.deleteAll();

            // 2. Mock Data Insert Karna (3 alag categories aur unke products)
            Category cat1 = Category.builder().name("Electronics").description("Gadgets").products(new ArrayList<>()).build();
            Category cat2 = Category.builder().name("Clothing").description("Apparel").products(new ArrayList<>()).build();

            categoryRepository.save(cat1);
            categoryRepository.save(cat2);

            Product p1 = Product.builder().name("Laptop").price(new BigDecimal("50000")).stock(10).category(cat1).build();
            Product p2 = Product.builder().name("Mobile").price(new BigDecimal("20000")).stock(20).category(cat1).build();
            Product p3 = Product.builder().name("T-Shirt").price(new BigDecimal("1000")).stock(30).category(cat2).build();

            productRepository.saveAll(List.of(p1, p2, p3));

            System.out.println("========== DATA DUMP COMPLETE ==========");

            // 📋 TEST 1: Demonstrating the N+1 Problem (Uncomment to see the worst case log)
//            System.out.println("\n🔥 TRIGGERING N+1 PROBLEM (Worst Case):");
//            List<Product> badProducts = productRepository.findAll(); // Standard built-in method
//            for (Product p : badProducts) {
//                // Yahan category ka naam fetch karte hi extra query fire hogi console me!
//                System.out.println("Product: " + p.getName() + " -> Category: " + p.getCategory().getName());
//            }

            // 📋 TEST 2: Testing the JOIN FETCH Optimization (The Fix)
            System.out.println("\n🛡️ TRIGGERING JOIN FETCH OPTIMIZATION (Best Case):");
            List<Product> goodProducts = productRepository.findAllWithCategoryFetch(); // Custom optimized query
            for (Product p : goodProducts) {
                System.out.println("Product: " + p.getName() + " -> Optimized Category: " + p.getCategory().getName());
            }
        };
    }
}
