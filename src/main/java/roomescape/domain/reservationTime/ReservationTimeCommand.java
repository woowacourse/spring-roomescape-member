package roomescape.domain.reservationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationTimeCommand(LocalTime startAt) {
    public ReservationTimeCommand(String startAt) {
        this(validateStartAt(startAt));
    }

    private static LocalTime validateStartAt(String startAt) {
        if(startAt == null) {
            throw new ReservationCommandException(ErrorMessage.INVALID_START_TIME_NULL);
        }
        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new ReservationCommandException(ErrorMessage.INVALID_START_TIME_FORMAT);
        }
    }
}
