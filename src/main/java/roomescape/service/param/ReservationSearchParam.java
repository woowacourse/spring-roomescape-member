package roomescape.service.param;

import java.time.LocalDate;

public record ReservationSearchParam(
        Long themeId, Long memberId, LocalDate from, LocalDate to
) {
}
