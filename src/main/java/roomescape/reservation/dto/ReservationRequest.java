package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date,
        Long themeId,
        Long timeId
) {
    public ReservationCommand toCommand(Long memberId) {
        return new ReservationCommand(memberId, date, themeId, timeId);
    }
}
