package roomescape.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.exception.DateTimeFormat;

public record ReservationTimeRequest(
        @NotNull(message = "시간을 입력해주세요.")
        @DateTimeFormat(pattern = "hh:mm", message = "시간 형식이 올바르지 않습니다.")
        String startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(parsedStartAt());
    }

    public LocalTime parsedStartAt() {
        return LocalTime.parse(startAt);
    }
}
