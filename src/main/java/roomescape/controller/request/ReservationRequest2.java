package roomescape.controller.request;

import java.time.LocalDate;

public record ReservationRequest2(Long memberId, LocalDate date, Long timeId, Long themeId) {
}
