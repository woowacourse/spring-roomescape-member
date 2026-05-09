package roomescape.reservation.controller;

import jakarta.validation.constraints.NotNull;

public record ReservationDeleteRequest(
        @NotNull(message = "이름을 입력해야 합니다.")
        String name) {
}
