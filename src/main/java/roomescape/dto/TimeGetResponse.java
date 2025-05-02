package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeGetResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    Boolean alreadyBooked) {

    public static TimeGetResponse from(ReservationTime reservationTime) {
        return new TimeGetResponse(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            reservationTime.getAlreadyBooked());
    }
}
