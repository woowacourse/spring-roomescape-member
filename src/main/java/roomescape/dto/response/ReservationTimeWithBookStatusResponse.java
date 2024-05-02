package roomescape.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeWithBookStatusResponse(Long id, String startAt, boolean booked) {
    private static final String TIME_FORMAT = "HH:mm";

    public static ReservationTimeWithBookStatusResponse fromReservationTime(ReservationTime reservationTime, boolean booked) {
        return new ReservationTimeWithBookStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern(TIME_FORMAT)),
                booked);
    }
}
