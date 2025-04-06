package utils;

import jakarta.ws.rs.BadRequestException;

public class QueryArgs {

    public QueryArgs() {  }

    public static void checkAllArgs(int page, int size, String sortOrder) {
        checkSortOrder(sortOrder);

        checkPageAndSize(page, size);

    }

    private static void checkSortOrder(String sortOrder) {
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder))
            throw new BadRequestException("Invalid sort order. Allowed values are 'asc' or 'desc'.");
    }

    private static void checkPageAndSize(int page, int size) {
        if (page < 0)
            throw new BadRequestException("Invalid page number: " + page);

        if (size <= 0)
            throw new BadRequestException("Invalid size number: " + size);
    }
}
