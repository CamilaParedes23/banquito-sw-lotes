package com.banquito.switchpagos.batch.repository;

import com.banquito.switchpagos.batch.model.BatchPaymentLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BatchPaymentLineRepository extends JpaRepository<BatchPaymentLine, UUID> {

    List<BatchPaymentLine> findByBatchIdOrderBySequenceNumberAsc(UUID batchId);
}
