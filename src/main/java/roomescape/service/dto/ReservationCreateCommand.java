package roomescape.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        UUID timeId,
        UUID themeId
) {
}
