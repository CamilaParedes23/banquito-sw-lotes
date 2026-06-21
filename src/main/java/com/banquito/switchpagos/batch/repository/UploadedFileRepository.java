package com.banquito.switchpagos.batch.repository;

import com.banquito.switchpagos.batch.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, UUID> {
}
