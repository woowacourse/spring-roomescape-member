package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record AvailableTimeRequest(
        @NotBlank(message = "테마 ID를 입력해주세요.")
        Long themeId,
        @NotBlank(message = "날짜를 입력해주세요.")
        LocalDate date) {
}
