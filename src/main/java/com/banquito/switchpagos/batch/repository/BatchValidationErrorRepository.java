package com.banquito.switchpagos.batch.repository;

import com.banquito.switchpagos.batch.model.BatchValidationError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BatchValidationErrorRepository extends JpaRepository<BatchValidationError, UUID> {

    List<BatchValidationError> findByBatchIdOrderByCreatedAtAsc(UUID batchId);
}
