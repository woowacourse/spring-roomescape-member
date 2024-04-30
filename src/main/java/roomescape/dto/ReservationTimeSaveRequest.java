package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record ReservationTimeSaveRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt
) {
        public ReservationTime toReservationTime() {
                return new ReservationTime(startAt);
        }
}
