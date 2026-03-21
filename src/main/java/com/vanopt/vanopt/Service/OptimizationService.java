package com.vanopt.vanopt.Service;

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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptimizationService {
    private final KnapsackSolver knapsackSolver;
    private final OptimizationRecordRepository repository;
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

    @Transactional
    public OptimizationResponseDto optimize(OptimizationRequestDto requestDto) {
        List<ShipmentDto> selectedShipments = knapsackSolver.knapsack(
                requestDto.availableShipments(), requestDto.maxVolume());

        OptimizationRecord record = mapper.requestDtoToRecord(
                requestDto, selectedShipments);
        OptimizationRecord saved = repository.save(record);

        return mapper.recordToResponseDto(saved);
    }
}
