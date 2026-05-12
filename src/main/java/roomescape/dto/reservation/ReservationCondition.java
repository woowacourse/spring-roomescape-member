package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationCondition(

        @NotBlank(message = "이름이 반드시 포함되어야 합니다.")
        String name
) {
}
