package roomescape.reservations.entity;

import java.time.LocalDate;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time
) {
    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
    }
}
