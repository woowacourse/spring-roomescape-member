package roomescape.dto.reservationTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddReservationTimeRequest(
        @NotBlank(message = "시작 시간은 받드시 포함되어야 합니다.")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시작 시간은 24시 형식의 HH:mm 여야 합니다.")
        String startAt
) {
}
