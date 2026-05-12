package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.EntityId;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        EntityId timeId,
        EntityId themeId
) {
}
