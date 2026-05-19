package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequestDto(
        @NotNull(message = "날짜는 비어 있을 수 없습니다.")
        LocalDate date,

        @NotNull(message = "예약 시간의 id는 비어 있을 수 없습니다.")
        Long timeId
) {

}
