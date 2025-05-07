package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull(message = "예약 시작 시간은 필수 입력값입니다.")
        @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt) {
}
