package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.model.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeBookedResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt,
        boolean alreadyBooked
) {
        public ReservationTimeBookedResponse(final ReservationTime reservationTime, final boolean alreadyBooked) {
                this(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
        }
}
