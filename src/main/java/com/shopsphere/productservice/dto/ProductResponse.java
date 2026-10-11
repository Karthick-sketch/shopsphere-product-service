package com.shopsphere.productservice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private String sku;
  private String category;
  private String imageUrl;
  private Long userId;
  private Boolean isAvailable;
}
