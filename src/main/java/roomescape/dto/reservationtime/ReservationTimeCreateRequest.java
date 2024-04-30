package roomescape.dto.reservationtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

public class ReservationTimeCreateRequest {

    private final String startAt;

    @JsonCreator
    private ReservationTimeCreateRequest(String startAt) {
        this.startAt = startAt;
    }

    public static ReservationTimeCreateRequest from(String startAt) {
        return new ReservationTimeCreateRequest(startAt);
    }

    public ReservationTime toDomain() {
        return new ReservationTime(
                null,
                ReservationStartAt.from(startAt)
        );
    }

    public String getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTimeCreateRequest other = (ReservationTimeCreateRequest) o;
        return Objects.equals(this.startAt, other.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt);
    }

    @Override
    public String toString() {
        return "ReservationTimeCreateRequestDto{" +
                "startAt='" + startAt + '\'' +
                '}';
    }
}
