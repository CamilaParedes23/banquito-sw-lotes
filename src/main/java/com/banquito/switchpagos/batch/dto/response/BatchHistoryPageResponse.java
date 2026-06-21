package com.banquito.switchpagos.batch.dto.response;

import java.util.ArrayList;
import java.util.List;

public class BatchHistoryPageResponse {

    private List<BatchHistoryItemResponse> content = new ArrayList<>();
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    public List<BatchHistoryItemResponse> getContent() { return content; }
    public void setContent(List<BatchHistoryItemResponse> content) { this.content = content; }
    public Long getTotalElements() { return totalElements; }
    public void setTotalElements(Long totalElements) { this.totalElements = totalElements; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
