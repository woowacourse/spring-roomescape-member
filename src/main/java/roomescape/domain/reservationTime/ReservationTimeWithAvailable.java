package roomescape.domain.reservationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationTimeConditionException;

public record ReservationTimeWithAvailable(long id, LocalTime startAt, boolean isAvailable) {
    public ReservationTimeWithAvailable(long id, String startAt, boolean isAvailable) {
        this(id, validateLocalDate(startAt), isAvailable);
    }

    public static LocalTime validateLocalDate(String startAt) {
        if(startAt == null || startAt.isBlank()) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_START_TIME_NULL);
        }

        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new ReservationTimeConditionException(ErrorMessage.INVALID_START_TIME_FORMAT);

        }
    }
}
