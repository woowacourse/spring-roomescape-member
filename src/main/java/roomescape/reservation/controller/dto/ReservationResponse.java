package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.dto.ReservationMember;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {
    public static ReservationResponse from(ReservationMember reservation) {
        return new ReservationResponse(
                reservation.reservation().getId(),
                reservation.member().getName(),
                reservation.reservation().getDate(),
                ReservationTimeResponse.from(reservation.reservation().getTime()),
                ThemeResponse.from(reservation.reservation().getTheme())
        );
    }
}
