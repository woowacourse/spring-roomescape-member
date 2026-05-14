package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReservationChangeScheduleDto(
        @NotNull(message = "dateId는 필수 입력값입니다.")
        Long dateId,

        @NotNull(message = "timeId는 필수 입력값입니다.")
        Long timeId
) {
}
