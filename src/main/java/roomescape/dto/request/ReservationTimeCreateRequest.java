package roomescape.dto.request;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

public record ReservationTimeCreateRequest(
        @NotNull(message = "예약 시간은 빈 값일 수 없습니다")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }
}
