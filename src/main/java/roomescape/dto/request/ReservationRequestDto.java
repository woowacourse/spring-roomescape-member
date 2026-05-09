package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank @Size(max = 20, message = "이름은 20자 이하여야 합니다")
        String name,
        @NotNull LocalDate date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId
) {
}
