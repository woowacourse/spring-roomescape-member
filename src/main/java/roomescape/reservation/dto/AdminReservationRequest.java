package roomescape.reservation.dto;

import java.time.LocalDate;

public record AdminReservationRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {
    // TODO : 검증 로직 필요
}
