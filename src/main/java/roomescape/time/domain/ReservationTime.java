package roomescape.time.domain;

import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
