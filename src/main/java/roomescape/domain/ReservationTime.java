package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(
            Long id,
            LocalTime startAt
    ) {
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(
            LocalTime startAt
    ) {
        return new ReservationTime(
                null,
                startAt
        );
    }

    public static ReservationTime retrieve(
            long id,
            LocalTime startAt
    ) {
        return new ReservationTime(
                id,
                startAt
        );
    }

    public ReservationTime with(long id) {
        return new ReservationTime(
                id,
                this.startAt
        );
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시간엔 시간 정보가 존재해야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
