package roomescape.date.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationDateSaveDto(
        @NotNull(message = "날짜는 필수 입력 항목입니다.")
        LocalDate date
) {
}
