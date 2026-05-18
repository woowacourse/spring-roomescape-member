package roomescape.domain.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.common.exception.InvalidReservationException;

@Component
public class ReservationValidator {

    public void validateFutureReservationDateTime(LocalDate date, LocalTime time, String message) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException(message);
        }
    }

    public void validateNotPastReservation(LocalDate date, LocalTime time, String message) {
        validateFutureReservationDateTime(date, time, message);
    }
}
