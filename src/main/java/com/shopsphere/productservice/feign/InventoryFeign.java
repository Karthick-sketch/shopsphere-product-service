package com.shopsphere.productservice.feign;

import com.shopsphere.productservice.config.FeignAuthConfig;
import com.shopsphere.productservice.dto.ProductIdsRequest;
import com.shopsphere.productservice.dto.inventory.AvailabilityResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  name = "SHOPSPHERE-INVENTORY-SERVICE",
  configuration = FeignAuthConfig.class
)
public interface InventoryFeign {
  @PostMapping("/api/inventory/availabilities")
  ResponseEntity<List<AvailabilityResponse>> getAvailabilities(
    @RequestBody ProductIdsRequest request
  );
}
