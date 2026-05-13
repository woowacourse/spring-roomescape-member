package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId) {
}
