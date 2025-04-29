package roomescape.reservationTime.domain.dto;

import java.time.LocalTime;

public record ReservationTimeReqDto(LocalTime startAt) {

    public ReservationTimeReqDto {
        startAt = startAt.withNano(0);
    }
}
