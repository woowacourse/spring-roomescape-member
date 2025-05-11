package roomescape.dto.reservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationRequestDto(

        @NotBlank(message = "날짜는 필수입니다.")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "날짜 형식이 틀렸습니다. yyyy-MM-dd (예: 2025-05-03) 형식으로 입력해야 합니다.")
        String date,

        @NotNull(message = "timeId는 필수 입니다.")
        Long timeId,

        @NotNull(message = "themeId는 필수 입니다.")
        Long themeId) {

}
