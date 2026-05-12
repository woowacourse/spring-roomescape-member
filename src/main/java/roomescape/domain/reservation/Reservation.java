package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;

import java.time.LocalDate;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }
}
