package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.ArgumentNullException;
import roomescape.exception.PastDateTimeReservationException;
import roomescape.reservationtime.ReservationTime;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;

    private Reservation(final Long id, final String name, final LocalDate date, final ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
    }

    private static void validateNull(String name, LocalDate date, ReservationTime reservationTime) {
        if (name == null || name.isBlank()) {
            throw new ArgumentNullException();
        }
        if (date == null) {
            throw new ArgumentNullException();
        }
        if (reservationTime == null) {
            throw new ArgumentNullException();
        }
    }

    public static Reservation of(final Long id, final String name, final LocalDate date,
                                 final ReservationTime reservationTime) {
        validateNull(name, date, reservationTime);
        return new Reservation(id, name, date, reservationTime);
    }

    public static Reservation createWithoutId(final String name, final LocalDate date,
                                              final ReservationTime reservationTime) {
        validateNull(name, date, reservationTime);
        validateDateTime(date, reservationTime);
        return Reservation.of(null, name, date, reservationTime);
    }

    private static void validateDateTime(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new PastDateTimeReservationException();
        }
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
}
