package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private static final long EMPTY_ID = 0L;

    private final Long id;
    private final Name name;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final Long id, final Name name, final LocalDate reservationDate,
                       final ReservationTime reservationTime, final Theme theme
    ) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.theme = theme;
        validateNull();
    }

    public static Reservation withoutId(
            final Name name,
            final LocalDate reservationDate,
            final ReservationTime reservationTime,
            final Theme theme
    ) {
        return new Reservation(EMPTY_ID, name, reservationDate, reservationTime, theme);
    }

    private void validateNull() {
        if (name == null || reservationDate == null || reservationTime == null || theme == null) {
            throw new IllegalArgumentException("Reservation field cannot be null");
        }
    }

    public boolean existId() {
        return id != EMPTY_ID;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationTimeId() {
        return reservationTime.getId();
    }

    public Name getName() {
        return name;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public LocalTime getReservationStartTime() {
        return reservationTime.getStartAt();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
