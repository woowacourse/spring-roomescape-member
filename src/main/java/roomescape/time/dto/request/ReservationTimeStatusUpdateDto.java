package roomescape.time.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeStatusUpdateDto(
        @NotNull(message = "isActive는 필수 입력입니다.")
        Boolean isActive
) {
}
