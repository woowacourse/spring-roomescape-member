package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeRequest(
        @NotNull(message = "시작 시간을 입력해 주세요.")
        @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt
) {

    public ReservationTime toTimeWithoutId() {
        return new ReservationTime(null, startAt);
    }

}
