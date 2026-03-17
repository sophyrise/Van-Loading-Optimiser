package com.vanopt.vanopt.Algorithm;

import com.vanopt.vanopt.DTO.ShipmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnapsackSolverTest {

    private KnapsackSolver solver;

    @BeforeEach
    void setUp() {
        solver = new KnapsackSolver();
    }

    @Test
    void shouldSelectOptimalCombination() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Parcel A", 5, new BigDecimal("120")),
                new ShipmentDto("Parcel B", 10, new BigDecimal("200")),
                new ShipmentDto("Parcel C", 3, new BigDecimal("80")),
                new ShipmentDto("Parcel D", 8, new BigDecimal("160"))
        );

        List<ShipmentDto> result = solver.knapsack(shipments, 15);

        int totalRevenue = result.stream()
                .map(ShipmentDto::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .intValue();
        int totalVolume = result.stream().mapToInt(ShipmentDto::volume).sum();

        assertThat(totalRevenue).isEqualTo(320);
    }

    @Test
    void shouldReturnEmptyWhenNoShipmentsFit() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Heavy", 20, new BigDecimal("500"))
        );

        List<ShipmentDto> result = solver.knapsack(shipments, 10);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyForEmptyInput() {
        List<ShipmentDto> result = solver.knapsack(Collections.emptyList(), 100);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSelectAllWhenEverythingFits() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Small A", 2, new BigDecimal("50")),
                new ShipmentDto("Small B", 3, new BigDecimal("60"))
        );

        List<ShipmentDto> result = solver.knapsack(shipments, 10);

        assertThat(result).hasSize(2);
        int totalVolume = result.stream().mapToInt(ShipmentDto::volume).sum();
        assertThat(totalVolume).isLessThanOrEqualTo(10);
    }

    @Test
    void shouldPickHigherRevenueWhenOnlyOneFits() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Cheap", 5, new BigDecimal("100")),
                new ShipmentDto("Expensive", 5, new BigDecimal("300"))
        );

        List<ShipmentDto> result = solver.knapsack(shipments, 5);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Expensive");
    }
}
