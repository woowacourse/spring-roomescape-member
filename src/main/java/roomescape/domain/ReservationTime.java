package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public record ReservationTime(
        UUID id,
        LocalTime startAt
) {

    public ReservationTime {
        validateId(id);
        validateStartAt(startAt);
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("예약 시간엔 식별자가 존재해야 합니다.");
        }
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
