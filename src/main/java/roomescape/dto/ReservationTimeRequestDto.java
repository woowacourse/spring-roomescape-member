package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequestDto(String startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(LocalTime.parse(startAt));
    }
}
