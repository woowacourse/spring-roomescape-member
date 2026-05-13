package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(
        @NotNull(message = "예약 시간은 필수값 입니다.")
        LocalTime startAt
) {
    public ReservationTime toReservationTime() {
        return ReservationTime.createWithoutId(startAt);
    }
}
