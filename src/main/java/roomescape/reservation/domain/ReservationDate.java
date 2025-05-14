package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.reservation.exception.ReservationFieldRequiredException;

public class ReservationDate {
    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException();
        }
    }

    public LocalDate getDate() {
        return date;
    }

}
