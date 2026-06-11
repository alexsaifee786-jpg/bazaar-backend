package com.bazaar.backend.controller;

import com.bazaar.backend.dto.ProductDTO;
import com.bazaar.backend.model.Product;
import com.bazaar.backend.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts()
    {
    List<Product> products= productRepository.findAllWithCategoryFetch();
        List<ProductDTO> productDto = products.stream().map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).stock(product.getStock()).categoryName(product.getCategory().getName()).build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(productDto);
    }
}
