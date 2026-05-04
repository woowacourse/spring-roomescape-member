package roomescape.reservation.repository.dto;

import java.time.LocalDate;

public record CreateReservationParams(
        String name,
        LocalDate date,
        Long timeId
) {
}
