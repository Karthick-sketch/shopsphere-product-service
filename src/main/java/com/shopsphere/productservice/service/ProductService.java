package com.shopsphere.productservice.service;

import com.shopsphere.productservice.entity.Product;
import com.shopsphere.productservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Product findById(Long id) {
    return productRepository
      .findById(id)
      .orElseThrow(() ->
        new RuntimeException("Product not found with id: " + id)
      );
  }

  public List<Product> findByCategory(String category) {
    return productRepository.findByCategory(category);
  }

  public List<Product> findByUserId(Long userId) {
    return productRepository.findByUserId(userId);
  }

  public Product create(Product product) {
    return productRepository.save(product);
  }

  public Product update(Long id, Product updated) {
    Product existing = findById(id);
    existing.setName(updated.getName());
    existing.setDescription(updated.getDescription());
    existing.setPrice(updated.getPrice());
    existing.setSku(updated.getSku());
    existing.setCategory(updated.getCategory());
    return productRepository.save(existing);
  }

  public void delete(Long id) {
    findById(id);
    productRepository.deleteById(id);
  }
}
