package roomescape.dto.request;

import java.time.LocalTime;

import jakarta.annotation.Nullable;
import roomescape.domain.ReservationTime;

public record ReservationTimeAddRequest(LocalTime startAt) {

    public ReservationTimeAddRequest {
        validate(startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("잘못된 요청입니다. 날짜를 확인해주세요.");
        }
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
