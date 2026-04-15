package com.example.ushi_backend.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor()
public class PaginationResponse<T> {

    private final List<T> data;
    private Integer page;
    private Integer limit;
    private Integer totalPage;
    private Long totalResult;

    public static <T> PaginationResponse<T> list(List<T> data, int page, int limit, int totalPage, long totalResult) {
        return PaginationResponse.<T>builder()
                .data(data)
                .page(page)
                .limit(limit)
                .totalPage(totalPage)
                .totalResult(totalResult)
                .build();
    }
}
