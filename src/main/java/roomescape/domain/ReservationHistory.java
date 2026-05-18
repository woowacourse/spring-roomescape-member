package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationHistory {

    private final Long id;
    private final String username;
    private final LocalDate reservationDate;
    private final LocalTime startAt;
    private final String themeName;

    public static ReservationHistory from(Reservation reservation) {
        return new ReservationHistory(
                null,
                reservation.username(),
                reservation.reservationDate(),
                reservation.reservationTime().startAt(),
                reservation.reservationTheme().name()
        );
    }

    public Long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public LocalDate reservationDate() {
        return reservationDate;
    }

    public LocalTime startAt() {
        return startAt;
    }

    public String themeName() {
        return themeName;
    }
}
