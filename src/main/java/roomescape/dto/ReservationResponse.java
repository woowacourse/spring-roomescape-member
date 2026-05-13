package roomescape.dto;

import java.time.LocalDate;
import roomescape.model.Reservation;

public record ReservationResponse(Long id, String name, LocalDate date, TimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.id(), reservation.name(), reservation.date(),
                TimeResponse.from(reservation.time()), ThemeResponse.from(reservation.theme()));
    }
}
