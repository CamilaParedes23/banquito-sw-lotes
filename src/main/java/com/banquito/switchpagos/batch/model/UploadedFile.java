package com.banquito.switchpagos.batch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"ARCHIVO_CARGADO\"")
public class UploadedFile {

    @Id
    @Column(name = "\"ID_ARCHIVO_CARGADO\"")
    private UUID uploadedFileId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"NOMBRE_ARCHIVO_ORIGINAL\"", nullable = false)
    private String originalFileName;

    @Column(name = "\"NOMBRE_ARCHIVO_SANITIZADO\"", nullable = false)
    private String sanitizedFileName;

    @Column(name = "\"EXTENSION_ARCHIVO\"", nullable = false)
    private String fileExtension;

    @Column(name = "\"HASH_ARCHIVO\"", nullable = false)
    private String fileHash;

    @Column(name = "\"TIPO_CONTENIDO\"")
    private String contentType;

    @Column(name = "\"TAMANO_ARCHIVO_BYTES\"", nullable = false)
    private Long fileSizeBytes;

    @Column(name = "\"REFERENCIA_ALMACENAMIENTO\"")
    private String storedReference;

    @Column(name = "\"FECHA_CARGA\"", nullable = false)
    private OffsetDateTime uploadedAt;

    public UploadedFile() {
    }

    public UploadedFile(UUID uploadedFileId) {
        this.uploadedFileId = uploadedFileId;
    }

    public UUID getUploadedFileId() {
        return uploadedFileId;
    }

    public void setUploadedFileId(UUID uploadedFileId) {
        this.uploadedFileId = uploadedFileId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSanitizedFileName() {
        return sanitizedFileName;
    }

    public void setSanitizedFileName(String sanitizedFileName) {
        this.sanitizedFileName = sanitizedFileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getStoredReference() {
        return storedReference;
    }

    public void setStoredReference(String storedReference) {
        this.storedReference = storedReference;
    }

    public OffsetDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(OffsetDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UploadedFile that)) {
            return false;
        }
        return Objects.equals(uploadedFileId, that.uploadedFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uploadedFileId);
    }

    @Override
    public String toString() {
        return "UploadedFile{uploadedFileId=" + uploadedFileId + ", batchId=" + batchId + "}";
    }
}
