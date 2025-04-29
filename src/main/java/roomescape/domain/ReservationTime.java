package roomescape.domain;

import java.time.LocalTime;
import roomescape.exception.InvalidReservationTimeException;

public class ReservationTime {

    private final Long id;
    private final LocalTime time;

    private void validate() {
        if (time == null) {
            throw new InvalidReservationTimeException("유효하지 않은 예약시간입니다.");
        }
    }

    public ReservationTime(Long id, LocalTime time) {
        this.id = id;
        this.time = time;
        validate();
    }

    public Long getId() {
        return id;
    }

    public LocalTime getTime() {
        return time;
    }
}
