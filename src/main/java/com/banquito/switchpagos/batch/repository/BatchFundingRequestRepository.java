package com.banquito.switchpagos.batch.repository;

import com.banquito.switchpagos.batch.model.BatchFundingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatchFundingRequestRepository extends JpaRepository<BatchFundingRequest, UUID> {
}
