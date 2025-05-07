package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.reservation.ReservationFieldRequiredException;

public class ReservationDate {
    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException("날짜");
        }
    }

    public LocalDate getDate() {
        return date;
    }

}
