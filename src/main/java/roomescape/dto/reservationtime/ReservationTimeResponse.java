package roomescape.dto.reservationtime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.time.ReservationTime;

public record ReservationTimeResponse(long id, String startAt, boolean alreadyBooked) {
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeResponse(long id, LocalTime time, boolean alreadyBooked) {
        this(id, time.format(DEFAULT_TIME_FORMATTER), alreadyBooked);
    }

    public static ReservationTimeResponse from(ReservationTime time, boolean alreadyBooked) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
