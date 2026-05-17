package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
    @NotNull(message = "날짜는 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,

    @NotNull(message = "시간 ID는 필수 입력값입니다.")
    Long timeId
) {
}
