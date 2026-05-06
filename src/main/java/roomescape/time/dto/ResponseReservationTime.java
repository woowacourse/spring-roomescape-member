package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ResponseReservationTime(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ResponseReservationTime from(ReservationTime time) {
        return new ResponseReservationTime(
                time.getId(),
                time.getStartAt()
        );
    }
}
