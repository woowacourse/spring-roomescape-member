package roomescape.domain;

import java.time.LocalDate;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public static Reservation createWithNullId(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation createWithId(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public Reservation appendId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }
}
