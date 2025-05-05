package roomescape.domain.reservation.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.reservation.entity.ReservationTime;

public record ReservationTimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public ReservationTime toEntity() {
        return ReservationTime.withoutId(startAt);
    }
}
