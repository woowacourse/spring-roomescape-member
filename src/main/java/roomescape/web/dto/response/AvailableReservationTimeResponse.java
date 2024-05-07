package roomescape.web.dto.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(Long id, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse of(ReservationTime time, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(
                time.getId(),
                time.getStartAt(),
                alreadyBooked
        );
    }

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm")
    public LocalTime startAt() {
        return startAt;
    }
}
