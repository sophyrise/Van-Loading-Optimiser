package com.vanopt.vanopt.Service.Algorithm;

import com.vanopt.vanopt.DTO.ShipmentDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KnapsackSolver {
    public List<ShipmentDto> knapsack(List<ShipmentDto> shipments, int maxVolume) {
        int n = shipments.size();

        if (n == 0 || maxVolume <= 0) {
            return Collections.emptyList();
        }

        BigDecimal[][] dp = new BigDecimal[n + 1][maxVolume + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= maxVolume; w++) {
                dp[i][w] = BigDecimal.ZERO;
            }
        }

        for(int i = 1; i <= n; i++) {
            ShipmentDto shipment = shipments.get(i - 1);
            int curShipmentVolume = shipment.volume();
            BigDecimal curShipmentRevenue = shipment.revenue();

            for(int w = 0; w <= maxVolume; w++) {
                dp[i][w] = dp[i - 1][w];
                if(curShipmentVolume <= w){
                    BigDecimal include = dp[i - 1][w - curShipmentVolume].add(curShipmentRevenue);
                    if (include.compareTo(dp[i][w]) > 0) {
                        dp[i][w] = include;
                    }
                }
            }
        }

        List<ShipmentDto> selectedShipments = new ArrayList<>();
        int w = maxVolume;

        for (int i = n; i >= 1; i--) {
            if (dp[i][w].compareTo(dp[i - 1][w]) != 0) {
                selectedShipments.add(shipments.get(i - 1));
                w -= shipments.get(i - 1).volume();
            }
        }

        return selectedShipments;
    }
}
