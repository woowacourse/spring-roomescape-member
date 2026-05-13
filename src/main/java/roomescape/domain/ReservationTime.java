package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime {
        if (Objects.isNull(startAt)) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
    }
}
