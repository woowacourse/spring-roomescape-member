package roomescape.admin.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record AdminReservationResponse(
        Long id,
        String name,
        LocalDate date,
        String themeName,
        String time
) {
    public static AdminReservationResponse from(Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme().getName(),
                reservation.getTime().getStartAt().toString()
        );
    }
}
