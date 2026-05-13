package roomescape.reservation.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        @Size(min = 1, max = 10, message = "예약자 이름은 10자 이하입니다.")
        String name,
        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,
        @NotNull(message = "예약 시간id는 필수입니다.")
        Long timeId,
        @NotNull(message = "테마 id는 필수입니다.")
        Long themeId
) {
}
