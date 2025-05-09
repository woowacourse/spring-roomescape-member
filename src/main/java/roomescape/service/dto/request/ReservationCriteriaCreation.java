package roomescape.service.dto.request;

import java.time.LocalDate;

public record ReservationCriteriaCreation(Long memberId, Long themeId, LocalDate from, LocalDate to) {
}
