package com.vanopt.vanopt.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shipment")
@Getter
@Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private OptimizationRecord request;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer volume;

    @Column(nullable = false)
    private BigDecimal revenue;

    @Column(nullable = false)
    private Boolean selected = false;
}
