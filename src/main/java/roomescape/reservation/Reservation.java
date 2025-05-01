package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.ArgumentNullException;
import roomescape.exception.PastDateTimeReservationException;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    private Reservation(final Long id, final String name, final LocalDate date, final ReservationTime reservationTime,
                        final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    private static void validateNull(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new ArgumentNullException();
        }
        if (date == null) {
            throw new ArgumentNullException();
        }
        if (reservationTime == null) {
            throw new ArgumentNullException();
        }
        if (theme == null) {
            throw new ArgumentNullException();
        }
    }

    public static Reservation of(final Long id, final String name, final LocalDate date,
                                 final ReservationTime reservationTime, final Theme theme) {
        validateNull(name, date, reservationTime, theme);
        return new Reservation(id, name, date, reservationTime, theme);
    }

    public static Reservation createWithoutId(final String name, final LocalDate date,
                                              final ReservationTime reservationTime, final Theme theme) {
        validateNull(name, date, reservationTime, theme);
        validateDateTime(date, reservationTime);
        return Reservation.of(null, name, date, reservationTime, theme);
    }

    private static void validateDateTime(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new PastDateTimeReservationException();
        }
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, date, reservationTime, theme);
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
