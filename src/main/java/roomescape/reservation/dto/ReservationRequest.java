package roomescape.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @FutureOrPresent(message = "예약 날짜는 오늘 또는 미래의 날짜여야 합니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수입니다.")
        @Positive(message = "올바른 시간 ID 형식이 아닙니다.")
        Long timeId
) {
}
