package roomescape.admin;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationSearchRequest(
        @NotNull Long memberId,
        @NotNull Long themeId,
        @NotNull LocalDate from,
        @NotNull LocalDate to
) {
}
