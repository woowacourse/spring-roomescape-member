package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Map.Entry;
import roomescape.model.ReservationTime;

public record ReservationTimeBookedResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt,
        boolean alreadyBooked
) {
        public static ReservationTimeBookedResponse from(final Entry<ReservationTime, Boolean> reservationTimeBooked) {
                return new ReservationTimeBookedResponse(
                        reservationTimeBooked.getKey().getId(),
                        reservationTimeBooked.getKey().getStartAt(),
                        reservationTimeBooked.getValue()
                );
        }
}
