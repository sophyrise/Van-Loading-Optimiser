package com.vanopt.vanopt.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "optimisation_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "max_volume", nullable = false)
    private Integer maxVolume;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shipment> shipments = new ArrayList<>();

}
