package roomescape.presentation.request;

import java.time.LocalTime;
import roomescape.service.param.CreateReservationTimeParam;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {
    public CreateReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시간 정보가 누락되었습니다.");
        }
    }

    public CreateReservationTimeParam toServiceParam() {
        return new CreateReservationTimeParam(startAt);
    }
}
