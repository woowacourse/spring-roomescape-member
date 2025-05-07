package roomescape.reservation.domain;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import roomescape.exception.ReservationException;
import roomescape.theme.domain.Theme;

@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time,
                       final Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationException("Name cannot be null or blank");
        }
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new ReservationException("Date cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new ReservationException("Date cannot be in the past");
        }
    }

    private void validateTime(final ReservationTime time) {
        if (time == null) {
            throw new ReservationException("Time cannot be null");
        }
    }

    private void validateTheme(final Theme theme) {
        if (theme == null) {
            throw new ReservationException("Theme cannot be null");
        }
    }
}
