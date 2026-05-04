package roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationTimeCommand(String startAt) {
    public ReservationTimeCommand {
        validateStartAt(startAt);
    }

    private static void validateStartAt(String startAt) {
        if(startAt.isBlank()) {
            throw new ReservationCommandException(ErrorMessage.INVALID_START_TIME_NULL);
        }
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new ReservationCommandException(ErrorMessage.INVALID_START_TIME_FORMAT);
        }
    }
}
