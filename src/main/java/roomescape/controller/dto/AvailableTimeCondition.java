package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableTimeCondition(
        @NotNull(message = "날짜를 입력해주세요.") LocalDate date,
        @NotNull(message = "테마를 입력해주세요.") long themeId
) {
}
