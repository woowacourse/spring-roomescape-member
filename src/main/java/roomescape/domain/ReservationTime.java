package roomescape.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final LocalTime endAt;

    private ReservationTime(final Long id, final LocalTime startAt, final LocalTime endAt) {
        validateStartAt(startAt);
        validateEndAt(endAt);

        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ReservationTime create(final LocalTime startAt, final LocalTime endAt) {
        return new ReservationTime(
                null,
                startAt,
                endAt
        );
    }

    public static ReservationTime of(final Long id, final LocalTime startAt, final LocalTime endAt) {
        return new ReservationTime(id, startAt, endAt);
    }


    private void validateStartAt(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간을 입력해야 합니다.");
        }
    }

    private void validateEndAt(final LocalTime endAt) {
        if (endAt == null) {
            throw new IllegalArgumentException("예약 종료 시간을 입력해야 합니다.");
        }
    }
}
