package roomescape.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(
        @Size(min = 2, max = 20, message = "[ERROR] 사용자 이름은 2자 이상 20자 이하입니다.")
        String name,

        LocalDate date,

        Long timeId,

        Long themeId) {
}
