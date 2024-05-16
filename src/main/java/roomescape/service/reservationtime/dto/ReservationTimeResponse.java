package roomescape.service.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import roomescape.domain.reservationtime.ReservationTime;

import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservationTimeResponse(Long id, LocalTime startAt, Boolean alreadyBooked) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), null);
    }

    public static ReservationTimeResponse of(ReservationTime reservationTime, Boolean alreadyBooked) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }
}
