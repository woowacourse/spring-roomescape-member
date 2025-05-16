package roomescape.time.service.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.time.entity.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull(message = "시간이 입력되지 않았습니다.")
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
