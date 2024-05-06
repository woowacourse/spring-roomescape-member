package roomescape.dto.reservationtime;

import java.util.Objects;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

public class ReservationTimeResponse {

    private final Long id;
    private final String startAt;

    private ReservationTimeResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse from(ReservationTime time) {
        ReservationStartAt reservationStartAt = time.getStartAt();
        return new ReservationTimeResponse(
                time.getId(),
                reservationStartAt.toStringTime()
        );
    }

    public static ReservationTimeResponse of(Long id, String startAt) {
        return new ReservationTimeResponse(id, startAt);
    }

    public Long getId() {
        return id;
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
        ReservationTimeResponse other = (ReservationTimeResponse) o;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.startAt, other.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }

    @Override
    public String toString() {
        return "TimeDto{" +
                "id=" + id +
                ", startAt='" + startAt + '\'' +
                '}';
    }
}
