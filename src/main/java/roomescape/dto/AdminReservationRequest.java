package roomescape.dto;

import java.time.LocalDate;

public record AdminReservationRequest(
        LocalDate date,
        Long themeId,
        Long timeId,
        Long memberId
) {
    public AdminReservationRequest {
        InputValidator.validateNotNull(date, themeId, timeId, memberId);
    }
}
