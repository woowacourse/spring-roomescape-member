package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservation.exception.InvalidReservationException;
import roomescape.reservation.exception.ReservationErrorCode;
import roomescape.reservationtime.domain.ReservationTime;

@Getter
@EqualsAndHashCode(of = "id")
public class Reservation {

    private static final int NAME_MAX_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validate(final String name, final LocalDate date, final ReservationTime time) {
        List<String> errors = new ArrayList<>();

        validateName(name, errors);
        validateDate(date, errors);
        validateTime(time, errors);

        if (!errors.isEmpty()) {
            throw new InvalidReservationException(errors);
        }
    }

    private void validateName(final String name, final List<String> errors) {
        if (name == null || name.isBlank() || name.length() >= NAME_MAX_LENGTH) {
            errors.add(ReservationErrorCode.RESERVATION_NAME_NOT_BLANK.getMessage());
        }
    }

    private void validateDate(final LocalDate date, final List<String> errors) {
        if (date == null) {
            errors.add(ReservationErrorCode.RESERVATION_DATE_NOT_NULL.getMessage());
        }
    }

    private void validateTime(final ReservationTime time, final List<String> errors) {
        if (time == null) {
            errors.add(ReservationErrorCode.RESERVATION_TIME_NOT_NULL.getMessage());
        }
    }

    public static Reservation createNew(final String name, final LocalDate date, ReservationTime time) {
        return new Reservation(null, name, date, time);
    }

    public static Reservation of(final long id, final String name, final LocalDate date, final ReservationTime time) {
        return new Reservation(id, name, date, time);
    }

    public Reservation withId(final long id) {
        return new Reservation(id, this.name, this.date, this.time);
    }

}
