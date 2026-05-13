package roomescape.web.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;
import roomescape.web.dto.theme.ThemeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme, ReservationStatus status) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getStatus()
        );
    }
}
