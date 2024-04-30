package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record SelectableTimeResponse(
        long timeId,

        @JsonFormat(pattern = "kk:mm")
        LocalTime startAt,

        boolean alreadyBooked
) {
    public static SelectableTimeResponse of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new SelectableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }
}
