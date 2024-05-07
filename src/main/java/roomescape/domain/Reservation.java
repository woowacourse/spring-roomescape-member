package roomescape.domain;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;
import roomescape.exception.ReservationFailException;

import java.time.LocalDate;

public class Reservation {
    private final String name;
    private final LocalDate date;
    private final TimeSlot time;
    private final Theme theme;

    public Reservation(final String name, final LocalDate date, final TimeSlot time, final Theme theme) {
        validateNameEmpty(name);
        validateReserveDateTime(date, time);
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateNameEmpty(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "name", "");
        }
    }

    private void validateReserveDateTime(final LocalDate date, final TimeSlot time) {
        if (time == null || (time.isTimeBeforeNow() && !date.isAfter(LocalDate.now()))) {
            throw new ReservationFailException("지나간 날짜와 시간으로 예약할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeSlot getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
