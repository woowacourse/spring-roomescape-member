package roomescape.controller.api.reservation.dto;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

public record ReservationSearchFilter(
        @Nullable Long memberId,
        @Nullable Long themeId,
        @Nullable LocalDate dateFrom,
        @Nullable LocalDate dateTo
) {
}
