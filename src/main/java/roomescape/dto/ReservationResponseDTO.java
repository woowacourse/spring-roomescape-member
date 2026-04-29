package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponseDTO(
        String name,
        LocalDate date,
        Long timeId
) {

    public static ReservationResponseDTO from(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getId()
        );
    }
}
