package roomescape.controller.reservation.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public CreateReservationRequest {
        if (name == null || date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("모든 값이 존재해야 합니다.");
        }
    }
}
