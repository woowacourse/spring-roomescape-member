package roomescape.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ScheduleRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "테마 ID는 필수입니다.")
        @Positive(message = "테마 ID는 양수여야 합니다.")
        Long themeId
) {
}
