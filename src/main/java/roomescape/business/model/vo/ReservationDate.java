package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

import java.time.LocalDate;
import java.time.Period;

import static roomescape.exception.ErrorCode.RESERVATION_DATE_PAST;
import static roomescape.exception.ErrorCode.RESERVATION_DATE_TOO_FAR_IN_FUTURE;

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
            throw new InvalidCreateArgumentException(RESERVATION_DATE_PAST);
        }
    }

    private static void validateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        if (minusDays > INTERVAL_FROM_NOW) {
            throw new InvalidCreateArgumentException(RESERVATION_DATE_TOO_FAR_IN_FUTURE, INTERVAL_FROM_NOW);
        }
    }
}
