package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import roomescape.model.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(
        @NotNull(message = "예약 시간이 비어 있습니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt
) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
