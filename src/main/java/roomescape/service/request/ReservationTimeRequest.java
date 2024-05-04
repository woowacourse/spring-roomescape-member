package roomescape.service.request;

import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import roomescape.domain.ReservationTime;
import roomescape.web.exception.TimeValid;

public record ReservationTimeRequest(@NotBlank(message = "생성할 예약 시간은 필수입니다.") @TimeValid String startAt) {

    public ReservationTime toDomain() {
        LocalTime time = LocalTime.parse(startAt);
        return new ReservationTime(time);
    }
}
