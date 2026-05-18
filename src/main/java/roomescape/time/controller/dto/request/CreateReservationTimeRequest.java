package roomescape.time.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateReservationTimeRequest(
        @NotNull(message = "시간 값은 필수입니다. 시간을 입력해주세요.")
        LocalTime startAt
) {
}
