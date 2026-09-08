package com.shopsphere.productservice.controller;

import com.shopsphere.productservice.entity.Product;
import com.shopsphere.productservice.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    return ResponseEntity.ok(productService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
  }

  @GetMapping("/category/{category}")
  public ResponseEntity<List<Product>> getByCategory(
    @PathVariable String category
  ) {
    return ResponseEntity.ok(productService.findByCategory(category));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Product>> getByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(productService.findByUserId(userId));
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      productService.create(product)
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(
    @PathVariable Long id,
    @RequestBody Product product
  ) {
    return ResponseEntity.ok(productService.update(id, product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
