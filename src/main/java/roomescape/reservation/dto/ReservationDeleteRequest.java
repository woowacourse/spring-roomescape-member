package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationDeleteRequest(
        @NotNull(message = "이름은 비어있을 수 없습니다.")
        String name) {
}
