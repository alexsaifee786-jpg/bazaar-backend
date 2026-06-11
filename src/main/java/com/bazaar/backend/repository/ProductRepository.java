package com.bazaar.backend.repository;
import com.bazaar.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ❌ NORMAL JpaRepository METHOD (This triggers N+1 Problem)
    // List<Product> findAll(); <-- Inbuilt hota hai

    // 🛡️ ADVANCED OPTIMIZED METHOD (The N+1 Fix)
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategoryFetch();
}

