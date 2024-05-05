package roomescape.service.dto.reservationtime;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @NotNull(message = "시작 시간은 비어있을 수 없습니다.") LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
