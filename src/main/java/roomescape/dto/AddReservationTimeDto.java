package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AddReservationTimeDto(LocalTime startAt) {

    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}

