package roomescape.domain;

import roomescape.exceptions.InvalidRequestBodyFieldException;
import roomescape.exceptions.InvalidReservationTimeException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private static void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidRequestBodyFieldException("시간 필드 값이 Null 입니다.");
        }
    }

    public boolean isSameTime(ReservationTime time) {
        return startAt.equals(time.getStartAt());
    }

    public void validateNotPast(LocalDate requestDate) {
        LocalDate today = LocalDate.now();
        if (requestDate.isBefore(today)) {
            throw new InvalidReservationTimeException("지나간 날짜에 예약을 등록할 수 없습니다.");
        }
        if (requestDate.isEqual(today) && startAt.isBefore(LocalTime.now())) {
            throw new InvalidReservationTimeException("지나간 시간에 예약을 등록할 수 없습니다.");
        }
    }

    public void validateNotDuplicated(LocalTime requestTime) {
        if (startAt.equals(requestTime)) {
            throw new InvalidReservationTimeException("중복된 시간을 예약할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
