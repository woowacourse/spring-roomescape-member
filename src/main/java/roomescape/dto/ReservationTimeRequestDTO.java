package roomescape.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeRequestDTO(
        @NotNull(message = "예약 시간은 필수입니다.") String startAt
) {
}
