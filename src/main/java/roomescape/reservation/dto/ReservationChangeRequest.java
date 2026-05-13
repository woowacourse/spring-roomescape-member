package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ReservationChangeRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        Long themeId,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId
) {
}
