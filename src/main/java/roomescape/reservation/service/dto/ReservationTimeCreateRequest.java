package roomescape.reservation.service.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservationTimeCreateRequest(
        @NotBlank(message = "올바르지 않은 시간입니다.") String startAt) {
}
