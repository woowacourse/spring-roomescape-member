package roomescape.schedule.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ScheduleRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        @FutureOrPresent(message = "예약 날짜는 오늘 또는 미래의 날짜여야 합니다.")
        LocalDate date,

        @NotNull(message = "테마 ID는 필수입니다.")
        @Positive(message = "테마 ID는 양수여야 합니다.")
        Long themeId
) {
}
