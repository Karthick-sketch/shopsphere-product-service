package com.shopsphere.productservice.repository;

import com.shopsphere.productservice.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findBySku(String sku);
  List<Product> findByCategory(String category);
  List<Product> findByUserId(Long userId);
}
