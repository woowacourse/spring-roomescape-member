package roomescape.reservation.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record CreateReservationTimeCommand(@NotNull(message = "시간을 입력해주세요.") LocalTime startAt) {

    public ReservationTime convertToReservationTime() {
        return new ReservationTime(this.startAt);
    }
}
