package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime withId(Long generatedId) {
        return ReservationTime.builder()
                .id(generatedId)
                .startAt(this.startAt)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (id == null) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
