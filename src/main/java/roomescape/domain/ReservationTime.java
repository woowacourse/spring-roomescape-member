package roomescape.domain;

import java.time.LocalTime;
import lombok.Getter;
import roomescape.global.exception.reservationtime.InvalidReservationTimeException;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createNew(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime from(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    private void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidReservationTimeException("예약 시간은 비어있을 수 없습니다.");
        }
    }
}
