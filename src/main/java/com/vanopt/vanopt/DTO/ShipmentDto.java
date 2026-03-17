package com.vanopt.vanopt.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ShipmentDto(
        @NotBlank(message = "Shipment name must not be blank")
        String name,

        @NotNull(message = "Volume is required")
        @Min(value = 1, message = "Volume must be at least 1")
        int volume,

        @NotNull(message = "Revenue is required")
        @Min(value = 0, message = "Revenue must be non-negative")
        BigDecimal revenue
) {
}
