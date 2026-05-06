package roomescape.entity;

import java.time.LocalDate;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time
) {
    public static Reservation createWithNullId(String name, LocalDate date, ReservationTime time) {
        return new Reservation(null, name, date, time);
    }

    public static Reservation createWithId(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
    }
}
