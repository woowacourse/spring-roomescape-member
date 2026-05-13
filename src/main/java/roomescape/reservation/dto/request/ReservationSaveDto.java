package roomescape.reservation.dto.request;

import roomescape.common.validation.annotation.NotBlank;
import roomescape.common.validation.annotation.NotNull;

public record ReservationSaveDto(

        @NotBlank(message = "name은 비어있을 수 없습니다.")
        String name,

        @NotNull(message = "dateId는 필수 입력값입니다.")
        Long dateId,

        @NotNull(message = "timeId는 필수 입력값입니다.")
        Long timeId,

        @NotNull(message = "themeId는 필수 입력값입니다.")
        Long themeId

) {
}
