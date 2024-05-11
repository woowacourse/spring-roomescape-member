package roomescape.reservation.dto;

import java.time.LocalDate;

public record CreateReservationAdminRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {
}
