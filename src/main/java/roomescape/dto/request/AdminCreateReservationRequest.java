package roomescape.dto.request;

import java.time.LocalDate;

public record AdminCreateReservationRequest(
        Long memberId,
        Long timeId,
        Long themeId,
        LocalDate date
) {
}
