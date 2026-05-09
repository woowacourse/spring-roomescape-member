package roomescape.admin.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.doamin.Theme;

public record AdminReservationResponse(Long id, String name,
                                       LocalDate date,
                                       String themeName,
                                       String time) {
    public static AdminReservationResponse from(Reservation reservation, Theme theme) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                theme.getName(),
                reservation.getTime().getStartAt().toString()
        );
    }
}
