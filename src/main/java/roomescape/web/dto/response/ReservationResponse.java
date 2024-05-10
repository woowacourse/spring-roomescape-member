package roomescape.web.dto.response;

import java.time.LocalDate;

import roomescape.domain.Reservation;

public record ReservationResponse(Long id, LocalDate date,
                                  ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
