package roomescape.dto.request;

import java.time.LocalDate;

public record AdminReservationAddRequest(Long memberId, LocalDate date, Long timeId, Long themeId) {
}
