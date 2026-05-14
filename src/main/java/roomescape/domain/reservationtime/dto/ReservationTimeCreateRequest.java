package roomescape.domain.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class ReservationTimeCreateRequest {

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    public ReservationTimeCreateRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
