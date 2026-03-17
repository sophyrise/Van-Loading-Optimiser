package com.vanopt.vanopt.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OptimizationRequestDto (
        @NotNull(message = "maxVolume is required")
        @Min(value = 1, message = "maxVolume must be at least 1")
        int maxVolume,

        @NotNull(message = "availableShipments is required")
        @NotEmpty(message = "availableShipments must not be empty")
        List<@Valid ShipmentDto> availableShipments
) {
}
