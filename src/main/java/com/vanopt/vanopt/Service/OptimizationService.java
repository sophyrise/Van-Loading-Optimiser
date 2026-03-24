package com.vanopt.vanopt.Service;

import com.vanopt.vanopt.Entity.Shipment;
import com.vanopt.vanopt.Repository.ShipmentRepository;
import com.vanopt.vanopt.Service.Algorithm.KnapsackSolver;
import com.vanopt.vanopt.DTO.OptimizationRequestDto;
import com.vanopt.vanopt.DTO.OptimizationResponseDto;
import com.vanopt.vanopt.DTO.ShipmentDto;
import com.vanopt.vanopt.Entity.OptimizationRecord;
import com.vanopt.vanopt.Repository.OptimizationRecordRepository;
import com.vanopt.vanopt.Mapper.OptimizationMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptimizationService {
    private final KnapsackSolver knapsackSolver;
    private final OptimizationRecordRepository repository;
    private final ShipmentRepository shipmentRepository;
    private final OptimizationMapper mapper;

    @Transactional
    public Optional<OptimizationResponseDto> findById(UUID requestId) {
        return repository.findById(requestId).map(mapper::recordToResponseDto);
    }

    @Transactional
    public List<OptimizationResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::recordToResponseDto)
                .toList();
    }

    public OptimizationResponseDto optimize(OptimizationRequestDto requestDto) {
        List<ShipmentDto> selectedShipments = knapsackSolver.knapsack(
                requestDto.availableShipments(), requestDto.maxVolume());

        int usedVolume = selectedShipments.stream()
                .mapToInt(ShipmentDto::volume)
                .sum();
        OptimizationRecord record = mapper.requestDtoToRecord(
                requestDto, selectedShipments);
        OptimizationRecord saved = repository.save(record);

        if(usedVolume < requestDto.maxVolume()) {
            List<Shipment> unSelectedShipments = shipmentRepository.findShipmentBySelected(false);
            List<ShipmentDto> unSelectedShipmentsDto = unSelectedShipments.stream()
                    .map(s -> {
                        return new ShipmentDto(s.getName(), s.getVolume(), s.getRevenue());
                    }).toList();
            List<ShipmentDto> newlySelectedShipments = knapsackSolver.knapsack(unSelectedShipmentsDto, requestDto.maxVolume() - usedVolume);
            Set<String> selectedNames = newlySelectedShipments.stream()
                    .map(ShipmentDto::name)
                    .collect(Collectors.toSet());

            for(int i = 0; i < unSelectedShipments.size(); i++) {
                Shipment curShipment = unSelectedShipments.get(i);
                if(selectedNames.contains(curShipment.getName())) {
                    curShipment.setSelected(true);
                    Shipment sh = shipmentRepository.save(curShipment);
                    saved.getShipments().add(sh);
                }
            }
        }
        return mapper.recordToResponseDto(saved);
    }
}
