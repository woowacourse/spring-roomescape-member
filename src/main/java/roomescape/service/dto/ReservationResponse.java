package roomescape.service.dto;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(Long id, String name, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getDate(), ReservationTimeResponse.of(reservation.getReservationTime()),
                ThemeResponse.of(reservation.getTheme()));
    }
}
