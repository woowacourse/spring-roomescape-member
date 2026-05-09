package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.doamin.Theme;

public record ReservationResponse(Long id,
                                  String date,
                                  String themeName,
                                  String themeDescription,
                                  String themeThumbnailUrl,
                                  String time) {
    public static ReservationResponse from(Reservation reservation, Theme theme) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate().toString(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl(),
                reservation.getTime().getStartAt().toString()
        );
    }
}
