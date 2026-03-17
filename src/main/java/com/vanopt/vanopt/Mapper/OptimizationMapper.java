package com.vanopt.vanopt.Mapper;

import com.vanopt.vanopt.DTO.OptimizationRequestDto;
import com.vanopt.vanopt.DTO.OptimizationResponseDto;
import com.vanopt.vanopt.DTO.ShipmentDto;
import com.vanopt.vanopt.Entity.OptimizationRecord;
import com.vanopt.vanopt.Entity.Shipment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OptimizationMapper {
    public OptimizationRecord recordDtoToRequest(OptimizationRequestDto requestDto,
                                                  List<ShipmentDto> selected) {
        OptimizationRecord record = new OptimizationRecord();
        record.setMaxVolume(requestDto.maxVolume());
        Set<String> selectedNames = selected.stream()
                .map(ShipmentDto::name)
                .collect(Collectors.toSet());

        List<ShipmentDto> allShipments = requestDto.availableShipments();
        List<Shipment> shipments = allShipments.stream()
                .map(s -> {
                    Shipment shipment = new Shipment();
                    shipment.setName(s.name());
                    shipment.setVolume(s.volume());
                    shipment.setRevenue(s.revenue());
                    shipment.setSelected(selectedNames.contains(s.name()));
                    shipment.setRequest(record);
                    return shipment;
                })
                .toList();
        record.setShipments(shipments);
        return record;
    }
    public OptimizationResponseDto recordToResponseDto(OptimizationRecord record) {
        List<ShipmentDto> selectedShipments = record.getShipments().stream()
                .filter(Shipment::getSelected)
                .map(s -> new ShipmentDto(s.getName(), s.getVolume(), s.getRevenue()))
                .toList();

        int totalVolume = selectedShipments.stream()
                .mapToInt(ShipmentDto::volume)
                .sum();
        BigDecimal totalRevenue = selectedShipments.stream()
                .map(ShipmentDto::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OptimizationResponseDto(
                record.getId(),
                selectedShipments,
                totalVolume,
                totalRevenue,
                record.getCreatedAt()
        );
    }

}
