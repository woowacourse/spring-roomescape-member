package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record AdminReservationCreateRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {
}
