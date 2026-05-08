package roomescape.domain;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Reservation {

    private final Long id;
    private final String username;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme reservationTheme;
    private final ReservationStatus status;

    public static Reservation pending(String username, LocalDate date) {
        return new Reservation(null, username, date, null, null, ReservationStatus.DRAFT);
    }

    public static Reservation of(long id, String username, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, username, date, time, theme, ReservationStatus.AVAILABLE);
    }

    public Reservation deleted () {
        return new Reservation(this.id, this.username, this.reservationDate, this.reservationTime, this.reservationTheme, ReservationStatus.DELETED);
    }

    public long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public LocalDate reservationDate() {
        return reservationDate;
    }

    public ReservationTime reservationTime() {
        return reservationTime;
    }

    public Theme reservationTheme() {
        return reservationTheme;
    }

    public ReservationStatus status() {
        return status;
    }
}
