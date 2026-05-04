package roomescape.reservationtime;

import java.time.LocalTime;

public record ReservationTimeResponse(long id, LocalTime startAt) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.id(),
                reservationTime.startAt()
        );
    }
}
