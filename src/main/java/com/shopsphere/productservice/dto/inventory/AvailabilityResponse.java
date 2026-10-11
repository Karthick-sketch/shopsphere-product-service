package com.shopsphere.productservice.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponse {

  private Long productId;
  private Boolean isAvailable;
}
