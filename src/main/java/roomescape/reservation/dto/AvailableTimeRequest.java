package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableTimeRequest(
        @NotNull LocalDate date,
        @NotNull(message = "테마를 선택해주세요.") Long themeId
) {
}
