package roomescape.time.dto.request;

import roomescape.common.validation.annotation.NotNull;

import java.time.LocalTime;

public record ReservationTimeSaveDto(
        @NotNull(message = "startAt은 필수 입력값입니다.")
        LocalTime startAt
) {
}
