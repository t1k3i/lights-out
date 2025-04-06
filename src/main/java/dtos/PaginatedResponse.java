package dtos;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int size,
        boolean hasNext
) {
    public static <T> PaginatedResponse<T> createPaginatedResponse(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean hasNext = page < totalPages - 1;
        return new PaginatedResponse<>(content, totalElements, totalPages, page, size, hasNext);
    }
}
