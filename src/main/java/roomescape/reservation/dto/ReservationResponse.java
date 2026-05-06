package roomescape.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.dto.response.ThemeDetailDto;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        LocalTime time,
        ThemeDetailDto theme,
        ReservationStatus status
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                reservation.time(),
                ThemeDetailDto.from(reservation.theme()),
                reservation.status()
        );
    }

}
