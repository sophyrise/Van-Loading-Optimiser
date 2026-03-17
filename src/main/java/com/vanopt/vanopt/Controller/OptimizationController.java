package com.vanopt.vanopt.Controller;

import com.vanopt.vanopt.DTO.OptimizationRequestDto;
import com.vanopt.vanopt.DTO.OptimizationResponseDto;
import com.vanopt.vanopt.Exception.ResourceNotFoundException;
import com.vanopt.vanopt.Service.OptimizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/van-optimizations")
@RequiredArgsConstructor
public class OptimizationController {
    private final OptimizationService optimizationService;

    @GetMapping("/{requestId}")
    public ResponseEntity<OptimizationResponseDto> getById(@PathVariable("requestId") UUID requestId) {
        return ResponseEntity.ok(optimizationService.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found: " + requestId)));
    }

    @GetMapping
    public ResponseEntity<List<OptimizationResponseDto>> getAll() {
        return ResponseEntity.ok(optimizationService.findAll());
    }

    @PostMapping
    public ResponseEntity<OptimizationResponseDto> optimize(@Valid @RequestBody OptimizationRequestDto request) {
        OptimizationResponseDto response = optimizationService.optimize(request);
        return ResponseEntity.ok(response);
    }

}
