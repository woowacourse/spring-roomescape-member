package roomescape.reservation.domain;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
    }
}
