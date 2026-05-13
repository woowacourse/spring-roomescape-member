package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotNull(message = "날짜 형식이 잘못되었습니다. (yyyy-MM-dd)")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId) {
}
