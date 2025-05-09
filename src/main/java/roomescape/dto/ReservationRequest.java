package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long userId
) {

}
