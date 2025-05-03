package roomescape.time.service.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull(message = "시간이 입력되지 않았습니다.")
        LocalTime startAt
) {
    public ReservationTimeEntity toEntity() {
        return new ReservationTimeEntity(0L, startAt);
    }
}
