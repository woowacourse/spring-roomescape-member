package roomescape.controller.dto;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record AdminReservationResponse(long id,
                                       String name,
                                       String date,
                                       String themeName,
                                       String time) {
    public static AdminReservationResponse from(Reservation reservation, Theme theme) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                theme.getName(),
                reservation.getTime().getStartAt().toString()
        );
    }
}