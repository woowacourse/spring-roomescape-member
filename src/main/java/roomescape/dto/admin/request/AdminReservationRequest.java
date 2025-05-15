package roomescape.dto.admin.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(

        @NotNull(message = "날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "테마는 필수 입니다.")
        Long themeId,

        @NotNull(message = "예약 시간은 필수 입니다.")
        Long timeId,

        @NotNull(message = "예약자는 필수 입니다.")
        Long memberId

) {
}
