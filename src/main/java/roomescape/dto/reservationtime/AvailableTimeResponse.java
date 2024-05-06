package roomescape.dto.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.time.ReservationTime;

public record AvailableTimeResponse(long id, @JsonFormat(pattern = "hh:mm") LocalTime startAt, boolean alreadyBooked) {
    public static AvailableTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }
}
