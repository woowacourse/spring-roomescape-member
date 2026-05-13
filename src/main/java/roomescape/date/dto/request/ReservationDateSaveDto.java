package roomescape.date.dto.request;

import roomescape.common.validation.annotation.NotNull;

import java.time.LocalDate;

public record ReservationDateSaveDto(
        @NotNull(message = "date는 필수 입력입니다.")
        LocalDate date
) {
}
