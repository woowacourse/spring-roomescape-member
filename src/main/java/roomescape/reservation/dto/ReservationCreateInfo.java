package roomescape.reservation.dto;

import java.time.LocalDateTime;

public record ReservationCreateInfo(
        Long userId,
        LocalDateTime startAt,
        Long themeId
) {
}
