package roomescape.domain.dto;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;

import java.time.LocalDate;

public record ReservationResponse(Long id, String name, LocalDate date, TimeSlot time, Theme theme) {
    public static ReservationResponse from(final Long id, final Reservation reservation) {
        return new ReservationResponse(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }
}
