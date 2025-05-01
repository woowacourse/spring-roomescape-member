package roomescape.controller.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  RoomThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                RoomThemeResponse.from(reservation.getTheme()));
    }
}
