package roomescape.domain;

import java.time.LocalDate;

public class ReservationDate {

    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        validateNonNull(date);
        this.date = date;
    }

    private void validateNonNull(LocalDate date) {
        if (date == null) {
            throw new NullPointerException("날짜는 null일 수 없습니다");
        }
    }
}
