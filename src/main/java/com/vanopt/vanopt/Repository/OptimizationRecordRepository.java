package com.vanopt.vanopt.Repository;

import com.vanopt.vanopt.Entity.OptimizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OptimizationRecordRepository extends JpaRepository<OptimizationRecord, UUID> {
}
