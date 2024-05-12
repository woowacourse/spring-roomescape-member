package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.reservationtime.ReservationTime;

public class AvailableReservationResponse {

    private final String startAt;
    private final Long timeId;
    private final boolean alreadyBooked;

    private AvailableReservationResponse(String startAt, Long timeId, boolean alreadyBooked) {
        this.startAt = startAt;
        this.timeId = timeId;
        this.alreadyBooked = alreadyBooked;
    }

    public static AvailableReservationResponse of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationResponse(
                reservationTime.getStartAt().toStringTime(),
                reservationTime.getId(),
                alreadyBooked
        );
    }

    public String getStartAt() {
        return startAt;
    }

    public Long getTimeId() {
        return timeId;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableReservationResponse other = (AvailableReservationResponse) o;
        return this.alreadyBooked == other.alreadyBooked
                && Objects.equals(this.startAt, other.startAt)
                && Objects.equals(this.timeId, other.timeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt, timeId, alreadyBooked);
    }

    @Override
    public String toString() {
        return "AvailableReservationResponse{" +
                "startAt='" + startAt + '\'' +
                ", timeId=" + timeId +
                ", alreadyBooked=" + alreadyBooked +
                '}';
    }
}
