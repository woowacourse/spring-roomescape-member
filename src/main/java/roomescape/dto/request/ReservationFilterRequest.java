package roomescape.dto.request;

import java.time.LocalDate;

public record ReservationFilterRequest(
    Long themeId,
    Long memberId,
    LocalDate dateFrom,
    LocalDate dateTo
) {

}
