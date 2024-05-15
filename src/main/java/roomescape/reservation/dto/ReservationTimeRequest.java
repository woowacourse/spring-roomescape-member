package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReservationTimeRequest(
        @NotBlank
        @Pattern(regexp = "^(?:[01]\\d|2[0-4]):(?:[0-5]\\d|60)$",
        message = "시간을 입력해 주세요.")
        String startAt
) {
}
