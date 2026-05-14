package roomescape.reservation.repository.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record UpdateReservationParams(
        Long reservationId,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public static UpdateReservationParams from(Reservation reservation) {
        return new UpdateReservationParams(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId());
    }
}
