package roomescape.dto.request;

import java.time.LocalDate;

public record SearchReservationRequest(
        Long themeId,
        Long memberId,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}
