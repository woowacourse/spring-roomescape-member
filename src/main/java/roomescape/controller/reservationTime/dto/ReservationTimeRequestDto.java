package roomescape.controller.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record ReservationTimeRequestDto(
        @NotNull(message = "시작 시각은 null일 수 없습니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTime convertToTime() {
            return new ReservationTime(startAt);
    }
}
