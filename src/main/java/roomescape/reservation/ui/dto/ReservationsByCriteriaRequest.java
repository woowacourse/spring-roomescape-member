package roomescape.reservation.ui.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationsByCriteriaRequest(
        @NotNull
        Long themeId,
        @NotNull
        Long memberId,
        @NotNull
        LocalDate dateFrom,
        @NotNull
        LocalDate dateTo
) {

}
