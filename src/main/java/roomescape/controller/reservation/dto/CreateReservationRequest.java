package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateReservationRequest(

        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 1, max = 5, message = "이름은 1자 이상, 5자 이하여야 합니다.")
        String name,

        @NotNull(message = "날짜는 필수입니다.")
        @FutureOrPresent(message = "날짜는 현재보다 미래여야 합니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {
}
