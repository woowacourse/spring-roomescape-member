package roomescape.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.ReservationTime;

public record TimeCreateRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTime createReservationTime() {
        return new ReservationTime(startAt);
    }
}
