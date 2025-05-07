package roomescape.dto.request;

import java.time.LocalDate;

public record CreateReservationByAdminRequest(
        Long memberId,
        Long timeId,
        Long themeId,
        LocalDate date
) {
}
