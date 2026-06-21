package com.banquito.switchpagos.batch.repository;

import com.banquito.switchpagos.batch.model.PaymentBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

public interface PaymentBatchRepository extends JpaRepository<PaymentBatch, UUID> {

    Boolean existsByFileNameAndFileHashAndStatusInAndReceivedAtAfter(
            String fileName,
            String fileHash,
            Collection<String> statuses,
            OffsetDateTime receivedAt);

    Boolean existsByFileHashAndStatusInAndReceivedAtAfter(
            String fileHash,
            Collection<String> statuses,
            OffsetDateTime receivedAt);

    Page<PaymentBatch> findByCompanyRucOrderByReceivedAtDesc(String companyRuc, Pageable pageable);

    Page<PaymentBatch> findAllByOrderByReceivedAtDesc(Pageable pageable);
}
