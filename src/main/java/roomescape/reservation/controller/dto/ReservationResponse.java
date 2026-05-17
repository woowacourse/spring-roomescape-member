package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        long id,
        String date,
        long themeId,
        String themeName,
        String themeDescription,
        String themeThumbnailUrl,
        long timeId,
        String time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate().toString(),
                reservation.getTheme().getId(),
                reservation.getTheme().getName(),
                reservation.getTheme().getDescription(),
                reservation.getTheme().getThumbnailUrl(),
                reservation.getTime().getId(),
                reservation.getTime().getStartAt().toString()
        );
    }
}
