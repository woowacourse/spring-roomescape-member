package roomescape.domain;

import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public static final LocalTime RESERVATION_START_TIME = LocalTime.of(12, 0);
    public static final LocalTime RESERVATION_END_TIME = LocalTime.of(22, 0);

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime {
        if (startAt.isBefore(RESERVATION_START_TIME) || startAt.isAfter(RESERVATION_END_TIME)) {
            throw new IllegalArgumentException("해당 시간은 예약 가능 시간이 아닙니다.");//TODO: 이후에 커스텀 예외 고려하기
        }
    }
}
