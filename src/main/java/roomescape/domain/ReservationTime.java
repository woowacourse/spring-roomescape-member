package roomescape.domain;

import lombok.Getter;
import roomescape.domain.dto.ReservationTimeCreateCommand;

import java.time.LocalTime;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final LocalTime startAt) {
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(ReservationTimeCreateCommand data) {
        return new ReservationTime(
                null,
                data.startAt()
        );
    }

    public ReservationTime saved(final Long id) {
        return new ReservationTime(
                id,
                startAt
        );
    }

    public static ReservationTime restore(final Long id, final LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }


    private void validateStartAt(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간을 입력해야 합니다.");
        }
    }
}
