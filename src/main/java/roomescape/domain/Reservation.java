package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.EmptyValueException;
import roomescape.exception.ExceptionCause;

public class Reservation {

    private final Long id;
    private final ReserverName name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time,
                        final Theme theme) {
        validateFields(name, date);
        this.id = id;
        this.name = new ReserverName(name);
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
        validateDateTime(date, time);
        return new Reservation(null, name, date, time, theme);
    }

    private static void validateDateTime(final LocalDate localDate, final ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, reservationTime.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
    }

    public void validateFields(String name, LocalDate date) {
        if (name.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_NAME);
        }
        if (date == null) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_DATE);
        }
    }

    public boolean isEqualId(final Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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
