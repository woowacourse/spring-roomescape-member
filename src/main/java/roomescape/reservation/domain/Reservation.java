package roomescape.reservation.domain;

import java.util.Objects;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final ReservationDate date, final ReservationTime time, final Theme theme) {
        validateInvalidName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final Long id, final Reservation reservation) {
        this(id, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    public Reservation(final String name, final ReservationDate date, final ReservationTime time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validateInvalidName(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new RoomEscapeException(ExceptionMessage.INVALID_USER_NAME);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationDate getReservationDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }
}
