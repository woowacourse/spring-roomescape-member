package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.model.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt
) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
