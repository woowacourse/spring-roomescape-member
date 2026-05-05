package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponseDTO(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public static ReservationResponseDTO from(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );
    }
}
