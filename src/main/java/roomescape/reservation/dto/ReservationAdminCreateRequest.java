package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationAdminCreateRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {
}
