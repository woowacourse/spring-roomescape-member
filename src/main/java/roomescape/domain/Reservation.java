package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time,
                        final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation load(final Long id, final String name, final LocalDate date, final ReservationTime time,
                                   final Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation create(final String name, final LocalDate date, final ReservationTime time,
                                     final Theme theme) {
        validateNameLength(name);
        validateDateTime(date, time);
        return new Reservation(null, name, date, time, theme);
    }

    private static void validateNameLength(final String name) {
        if (name.length() > 10) {
            throw new IllegalArgumentException("예약자명은 10자 이하여야합니다.");
        }
    }

    private static void validateDateTime(final LocalDate localDate, final ReservationTime reservationTime) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
        if (localDate.isEqual(LocalDate.now()) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
    }

    public boolean isEqualId(final Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
