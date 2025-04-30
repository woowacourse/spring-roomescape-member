package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.service.ThemeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time, ThemeResponse theme) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                ),
                new ThemeResponse(reservation.getTheme().getId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnail())
        );
    }
}
