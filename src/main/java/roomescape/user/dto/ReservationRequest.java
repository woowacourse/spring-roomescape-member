package roomescape.user.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(
    @Size(max = 255, message = "[ERROR] 이름은 255자를 초과할 수 없습니다.")
    String name,
    LocalDate date,
    Long timeId,
    Long themeId
) {

}
