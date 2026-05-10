package roomescape.controller.dto;

import roomescape.global.exception.InvalidReservationPagingException;

public record ReservationPagingQuery(
        int page,
        int size
) {

    private static final int MIN_PAGE = 0;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    public ReservationPagingQuery {
        validatePage(page);
        validateSize(size);
        validateOffset(page, size);
    }

    public int offset() {
        return page * size;
    }

    private static void validatePage(int page) {
        if (page < MIN_PAGE) {
            throw new InvalidReservationPagingException("페이지 번호는 " + MIN_PAGE + " 이상이어야 합니다.");
        }
    }

    private static void validateSize(int size) {
        if (size < MIN_PAGE_SIZE) {
            throw new InvalidReservationPagingException("페이지 크기는 " + MIN_PAGE_SIZE + " 이상이어야 합니다.");
        }
        if (size > MAX_PAGE_SIZE) {
            throw new InvalidReservationPagingException("페이지 크기는 " + MAX_PAGE_SIZE + "을 넘을 수 없습니다.");
        }
    }

    private static void validateOffset(int page, int size) {
        if (page > Integer.MAX_VALUE / size) {
            throw new InvalidReservationPagingException("페이지 번호가 너무 큽니다.");
        }
    }
}
