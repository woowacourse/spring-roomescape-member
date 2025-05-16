package roomescape.domain.entity;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ReservationTime {

    private Long id;

    @NonNull
    private LocalTime startAt;

    public ReservationTime(@NonNull final LocalTime startAt) {
        this.id = null;
        this.startAt = startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationTime other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }

}
