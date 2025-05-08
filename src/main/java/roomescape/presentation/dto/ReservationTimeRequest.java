package roomescape.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.business.domain.ReservationTime;

public record ReservationTimeRequest(@NotNull(message = "시간을 입력해주세요.") LocalTime startAt) {

    public ReservationTime convertToReservationTime() {
        return new ReservationTime(this.startAt);
    }
}
