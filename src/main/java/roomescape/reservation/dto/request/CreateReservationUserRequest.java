package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record CreateReservationUserRequest(LocalDate date, Long timeId, Long themeId) {
    public CreateReservationUserRequest {
        if (date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("올바른 예약이 아닙니다.");
        }
    }
}
