package roomescape.service.dto;

import java.time.LocalDate;

public record CreateReservationCommand(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
