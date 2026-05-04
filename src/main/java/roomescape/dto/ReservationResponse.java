package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
               new ReservationTimeResponse(
                       reservation.getTime().getId(),
                       reservation.getTime().getStartAt()
               ),
                new ThemeResponse(
                        reservation.getTheme().getId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnail())
        );
    }
}
