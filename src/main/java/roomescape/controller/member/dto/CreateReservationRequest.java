package roomescape.controller.member.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        Long memberId,
        Long themeId,
        LocalDate date,
        Long timeId) {
}
