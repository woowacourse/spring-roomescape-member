package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreationRequest(
        @NotBlank(message = "[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.")
        String name,
        @NotNull(message = "[ERROR] 날짜는 빈 값을 허용하지 않습니다.")
        LocalDate date,
        @NotNull(message = "[ERROR] 시간은 빈 값을 허용하지 않습니다.")
        Long timeId,
        @NotNull(message = "[ERROR] 테마는 빈 값을 허용하지 않습니다.")
        Long themeId
) {

}
