package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationException;

public record Reservation(long id, String name, LocalDate date, ReservationTime time, ReservationTheme reservationTheme) {
    public Reservation(long id, String name, String date, ReservationTime time, ReservationTheme theme) {
        this(id, name, validateDate(date), time, theme);
    }

    public static LocalDate validateDate(String date) {
        if(date == null || date.isBlank()) {
            throw new ReservationException(ErrorMessage.INVALID_DATE_NULL);
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ReservationException(ErrorMessage.INVALID_DATE_FORMAT);
        }
    }
}
