package roomescape.controller.request;

import java.time.LocalDate;

public record ReservationRequest2(LocalDate date, Long memberId, Long timeId, Long themeId) {
}
