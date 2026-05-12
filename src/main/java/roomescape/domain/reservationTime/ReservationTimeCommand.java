package roomescape.domain.reservationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

public record ReservationTimeCommand(LocalTime startAt) {

}
