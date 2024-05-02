package roomescape.dto.reservationtime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.time.ReservationTime;

public record ReservationTimeResponse(long id, String startAt) {
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeResponse(long id, LocalTime time) {
        this(id, time.format(DEFAULT_TIME_FORMATTER));
    }

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }
}
