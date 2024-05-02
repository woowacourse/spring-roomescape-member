package roomescape.domain;

import java.time.LocalDate;

public class ReservationDate {

    private final LocalDate value;

    public ReservationDate(final LocalDate value) {
        validateDate(value);
        this.value = value;
    }

    private void validateDate(final LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("예약 날짜는 공백을 입력할 수 없습니다.");
        }
    }

    public LocalDate getValue() {
        return value;
    }
}
