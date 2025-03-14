package com.iabacus.salespro.web.common;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageResponse<T> {

    private final long page;
    private final long size;
    private final long totalPages;
    private final long totalElements;
    private final List<T> content;

    public PageResponse(Page<T> page) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.content = page.getContent();
    }

}