package com.shopsphere.productservice.service;

import com.shopsphere.productservice.dto.*;
import com.shopsphere.productservice.dto.inventory.AvailabilityResponse;
import com.shopsphere.productservice.entity.Product;
import com.shopsphere.productservice.feign.InventoryFeign;
import com.shopsphere.productservice.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  private final InventoryFeign inventoryFeign;

  public List<ProductResponse> findAll() {
    List<Product> products = productRepository.findAll();
    ProductIdsRequest request = new ProductIdsRequest(
      products
        .stream()
        .map(p -> p.getId())
        .toList()
    );
    List<AvailabilityResponse> availabilities = inventoryFeign
      .getAvailabilities(request)
      .getBody();
    return toProductResponseList(products, availabilities);
  }

  public Product findById(Long id) {
    return productRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found"));
  }

  public ProductResponse findProductResponseById(Long id) {
    Product product = findById(id);

    AvailabilityResponse availability = inventoryFeign
      .getAvailabilities(new ProductIdsRequest(List.of(id)))
      .getBody()
      .get(0);
    return toProductResponse(product, availability.getIsAvailable());
  }

  public List<Product> findByCategory(String category) {
    return productRepository.findByCategory(category);
  }

  public List<Product> findByUserId(Long userId) {
    return productRepository.findByUserId(userId);
  }

  public List<Product> findByIds(ProductIdsRequest request) {
    return productRepository.findAllById(request.getProductIds());
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

  private List<ProductResponse> toProductResponseList(
    List<Product> products,
    List<AvailabilityResponse> availabilities
  ) {
    Map<Long, Boolean> availabilityMap = availabilities
      .stream()
      .collect(
        Collectors.toMap(
          avail -> avail.getProductId(),
          avail -> avail.getIsAvailable()
        )
      );
    return products
      .stream()
      .map(p -> toProductResponse(p, availabilityMap.get(p.getId())))
      .toList();
  }

  private ProductResponse toProductResponse(
    Product product,
    Boolean isAvailable
  ) {
    return ProductResponse.builder()
      .id(product.getId())
      .name(product.getName())
      .description(product.getDescription())
      .price(product.getPrice())
      .sku(product.getSku())
      .category(product.getCategory())
      .imageUrl(product.getImageUrl())
      .userId(product.getUserId())
      .isAvailable(isAvailable)
      .build();
  }

  // ProductSummary
  public List<ProductSummary> findSummaryByIds(ProductIdsRequest request) {
    return findByIds(request).stream().map(this::toSummary).toList();
  }

  private ProductSummary toSummary(Product p) {
    return new ProductSummary(p.getId(), p.getName(), p.getImageUrl());
  }

  // ProductInfo
  public ProductInfo findInfoById(Long id) {
    return toInfo(findById(id));
  }

  public List<ProductInfo> findInfosByIds(ProductIdsRequest productIds) {
    return findByIds(productIds).stream().map(this::toInfo).toList();
  }

  private ProductInfo toInfo(Product p) {
    return new ProductInfo(
      p.getId(),
      p.getName(),
      p.getPrice(),
      p.getImageUrl()
    );
  }
}
