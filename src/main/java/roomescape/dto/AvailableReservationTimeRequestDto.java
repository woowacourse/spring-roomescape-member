package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequestDto(

        @NotBlank(message = "날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "themeId는 필수 입니다.")
        Long themeId) {
}
