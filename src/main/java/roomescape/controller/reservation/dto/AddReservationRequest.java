package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AddReservationRequest(

        @NotNull(message = "멤버 ID는 필수입니다.")
        Long memberId,

        @NotNull(message = "날짜는 필수입니다.")
        @FutureOrPresent(message = "날짜는 현재보다 미래여야 합니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {
}
