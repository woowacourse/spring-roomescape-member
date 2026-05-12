package roomescape.reservationtime.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull(message = "시작 시간을 입력해주세요")
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime startAt) {
}
