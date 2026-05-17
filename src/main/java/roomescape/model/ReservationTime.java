package roomescape.model;

import java.time.LocalTime;
import roomescape.exception.BadRequestException;
import roomescape.exception.ErrorCode;
import roomescape.exception.UnprocessableEntityException;

public class ReservationTime {

    private static final int START_MINUTE = 0;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
        validateStartAt();
    }

    private void validateStartAt() {
        if (startAt == null) {
            throw new BadRequestException(ErrorCode.TIME_NULL);
        }
        if (startAt.getMinute() != START_MINUTE) {
            throw new UnprocessableEntityException(ErrorCode.TIME_NOT_ON_THE_HOUR);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

}
