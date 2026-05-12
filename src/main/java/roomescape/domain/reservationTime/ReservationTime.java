package roomescape.domain.reservationTime;

import java.time.LocalTime;

public record ReservationTime(long id, LocalTime startAt) {
    public static ReservationTime from(long id, ReservationTimeCommand command) {
        return new ReservationTime(id, command.startAt());
    }
}
