package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponse(Long id, String name,
                                  LocalDate date,
                                  String themeName,
                                  String themeDescription,
                                  String thumbnailUrl,
                                  String time) {
    public static ReservationResponse from(Reservation reservation, Theme theme) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl(),
                reservation.getTime().toString()
        );
    }
}
