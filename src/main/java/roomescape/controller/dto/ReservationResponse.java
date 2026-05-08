package roomescape.controller.dto;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponse(long id,
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