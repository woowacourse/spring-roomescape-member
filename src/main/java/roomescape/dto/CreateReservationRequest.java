package roomescape.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        String name,
        Long themeId,
        LocalDate date,
        Long timeId
) {
}
