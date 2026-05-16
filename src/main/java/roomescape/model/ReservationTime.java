package roomescape.model;

import java.time.LocalTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime withValidate(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        return new ReservationTime(id, startAt);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt.getMinute() != 0) {
            throw new RoomescapeException(ErrorCode.TIME_WRONG_STARTAT);
        }
    }

    public boolean isValid() {
        return startAt.getMinute() == 0;
    }

    public Long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }
}
