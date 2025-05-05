package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeCreateRequestDto(
        @NotNull(message = "예약시간이 없습니다.") LocalTime startAt
) {

    public ReservationTime createWithoutId() {
        return new ReservationTime(null, startAt);
    }
}
