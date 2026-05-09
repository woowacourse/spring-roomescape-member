package roomescape.holiday.exception;

import roomescape.error.ErrorCode;
import roomescape.error.NotFoundException;

public class HolidayNotFoundException extends NotFoundException {
    private final Long id;

    public HolidayNotFoundException(Long id) {
        super(ErrorCode.HOLIDAY_NOT_FOUND, "휴일이 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
