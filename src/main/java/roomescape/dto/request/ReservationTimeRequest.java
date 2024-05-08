package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull(message = "원하는 시간을 지정해주세요") LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
