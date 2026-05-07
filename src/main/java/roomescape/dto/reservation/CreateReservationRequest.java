package roomescape.dto.reservation;

import java.time.LocalDate;

public record CreateReservationRequest(
        String name,
        Long themeId,
        LocalDate date,
        Long timeId
) {
}
