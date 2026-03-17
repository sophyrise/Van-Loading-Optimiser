package com.vanopt.vanopt.Repository;

import com.vanopt.vanopt.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
}
