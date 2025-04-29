package roomescape.domain;

import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this.startAt = startAt;
    }

    private void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간은 null이 될 수 없습니다.");
        }
    }
}
