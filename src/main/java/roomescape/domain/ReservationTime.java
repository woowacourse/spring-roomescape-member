package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.DomainException;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime {
        if (Objects.isNull(startAt)) {
            throw new DomainException("예약 시간은 필수입니다.");
        }
    }
}
