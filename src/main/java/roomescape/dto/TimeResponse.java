package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    Boolean alreadyBooked) {

    public static TimeResponse from(ReservationTime reservationTime) {
        return new TimeResponse(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            reservationTime.getAlreadyBooked());
    }
}
