package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record CreateReservationRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {
}
