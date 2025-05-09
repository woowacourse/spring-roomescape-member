package roomescape.dto.request;

import java.time.LocalDate;

public record CreateAdminReservationRequest(
        Long memberId,
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
