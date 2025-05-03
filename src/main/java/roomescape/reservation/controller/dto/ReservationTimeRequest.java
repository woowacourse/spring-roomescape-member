package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeRequest(@NotNull(message = "시간을 입력해주세요.") LocalTime startAt) {

    public ReservationTime converToReservationTime() {
        return new ReservationTime(this.startAt);
    }
}
