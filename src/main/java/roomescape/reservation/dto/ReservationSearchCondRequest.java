package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.annotaions.ValidDateRange;

@ValidDateRange
@NotNull
public record ReservationSearchCondRequest(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
}

