package roomescape.reservation.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReservationCancelDto(
        @NotBlank(message = "name(예약자 이름)은 비어있을 수 없습니다.")
        String name
) {
}
