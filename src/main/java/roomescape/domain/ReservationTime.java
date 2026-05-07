package roomescape.domain;

import lombok.Getter;
import roomescape.domain.dto.ReservationTimeCreateData;

import java.time.LocalTime;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final LocalTime endAt;

    private ReservationTime(final Long id, final LocalTime startAt, final LocalTime endAt) {
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ReservationTime create(ReservationTimeCreateData data) {
        return new ReservationTime(
                null,
                data.startAt(),
                data.endAt()
        );
    }

    public ReservationTime saved(final Long id) {
        return new ReservationTime(
                id,
                startAt,
                endAt
        );
    }

    public static ReservationTime restore(final Long id, final LocalTime startAt, final LocalTime endAt) {
        return new ReservationTime(id, startAt, endAt);
    }


    private void validateStartAt(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간을 입력해야 합니다.");
        }
    }
}
