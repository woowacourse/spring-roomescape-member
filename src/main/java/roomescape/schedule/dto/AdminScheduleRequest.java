package roomescape.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record AdminScheduleRequest(
        @NotNull(message = "테마 ID는 필수입니다.")
        @Positive(message = "테마 ID는 양수여야 합니다.")
        Long themeId,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "예약 날짜는 오늘 또는 미래의 날짜여야 합니다.")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time
) {
}
