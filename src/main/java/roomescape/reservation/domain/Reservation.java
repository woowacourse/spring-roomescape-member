package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;
import roomescape.member.domain.ReserverName;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

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
            throw new BadRequestException(ExceptionCause.RESERVATION_IMPOSSIBLE_FOR_PAST);
        }
    }

    public void validateFields(String name, LocalDate date) {
        if (name.isBlank()) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_NAME);
        }
        if (date == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_DATE);
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
