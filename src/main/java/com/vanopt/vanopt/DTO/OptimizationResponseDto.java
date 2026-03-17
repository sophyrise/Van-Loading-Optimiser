package com.vanopt.vanopt.DTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OptimizationResponseDto(
        UUID requestId,
        List<ShipmentDto> selectedShipments,
        Integer totalVolume,
        BigDecimal totalRevenue,
        OffsetDateTime createdAt
) {
}
