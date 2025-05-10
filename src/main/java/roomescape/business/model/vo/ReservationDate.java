package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

import java.time.LocalDate;
import java.time.Period;

public record ReservationDate(
        LocalDate value
) {
    private static final int INTERVAL_FROM_NOW = 7;

    public ReservationDate {
        validateInterval(value);
        validateNotPast(value);
    }

    private static void validateNotPast(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidCreateArgumentException("과거 날짜로 예약할 수 없습니다.");
        }
    }

    private static void validateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        if (minusDays > INTERVAL_FROM_NOW) {
            throw new InvalidCreateArgumentException("일주일 전부터 예약할 수 있습니다.");
        }
    }
}
