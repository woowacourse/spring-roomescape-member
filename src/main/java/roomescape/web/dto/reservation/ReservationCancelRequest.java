package roomescape.web.dto.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationCancelRequest(
        @NotBlank(message = "예약자 이름 정보는 필수 값입니다.")
        String name
) {
}
