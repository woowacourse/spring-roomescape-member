package roomescape.domain;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Reservation {

    private final Long id;
    private final String username;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    public static Reservation pending(String username, LocalDate date) {
        return new Reservation(null, username, date, null);
    }

    public static Reservation create(long id, String username, LocalDate date, ReservationTime time) {
        return new Reservation(id, username, date, time);
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

    public long id() {
        return id;
    }
}
