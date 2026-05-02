package roomescape.service.dto.request;

import java.time.LocalDate;

public record ReservationCreateData(
        String name,
        LocalDate date,
        Long timeId
) {
}
