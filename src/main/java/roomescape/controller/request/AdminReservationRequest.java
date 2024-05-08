package roomescape.controller.request;

import java.time.LocalDate;

public record AdminReservationRequest(Long memberId, LocalDate date, Long timeId, Long themeId) {
}
