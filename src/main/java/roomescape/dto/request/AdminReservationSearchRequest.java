package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationSearchRequest(
        @NotNull(message = "memberId를 입력해주세요.")
        Long memberId,

        @NotNull(message = "themeId를 입력해주세요.")
        Long themeId,

        @NotNull(message = "dateFrom를 입력해주세요.")
        LocalDate dateFrom,

        @NotNull(message = "dateTo를 입력해주세요.")
        LocalDate dateTo
) {
}
