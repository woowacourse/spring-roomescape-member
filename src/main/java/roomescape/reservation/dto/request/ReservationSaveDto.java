package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationSaveDto(

        @NotBlank(message = "name(예약자 이름)은 비어있을 수 없습니다.")
        String name,

        @NotNull(message = "dateId는 필수 입력값입니다.")
        Long dateId,

        @NotNull(message = "timeId는 필수 입력값입니다.")
        Long timeId,

        @NotNull(message = "themeId는 필수 입력값입니다.")
        Long themeId

) {
}
