package roomescape.global.pagination;

import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;

public class PaginationUtil {

    private static final int DEFAULT_PAGE_SIZE = 10;

    public static PageInfo calculatePageInfo(int page, int totalItems) {
        return calculatePageInfo(page, totalItems, DEFAULT_PAGE_SIZE);
    }

    public static PageInfo calculatePageInfo(int page, int totalItems, int pageSize) {
        int divided = totalItems / pageSize;
        int totalPage = (totalItems % pageSize == 0) ?
                divided : divided + 1;

        validatePage(page, totalItems, totalPage);

        int start = (page - 1) * pageSize + 1;
        int end = start + pageSize - 1;

        return new PageInfo(totalPage, start, end);
    }

    private static void validatePage(int page, int totalItems, int totalPage) {
        if (totalItems > 0 && (page < 1 || page > totalPage)) {
            throw new ResourceNotFoundException("해당하는 페이지가 없습니다");
        }
    }

    public record PageInfo(int totalPage, int startIdx, int endIdx) {
    }
}
