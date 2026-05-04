package roomescape.user.dto;

import java.time.LocalDate;

public record ReservationRequest(
    String name,
    LocalDate date,
    Long timeId,
    Long themeId
) {

}
