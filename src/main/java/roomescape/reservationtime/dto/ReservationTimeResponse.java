package roomescape.reservationtime.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String time) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }

    public static ReservationTimeResponse from(Long id, LocalTime startAt) {
        return new ReservationTimeResponse(
                id,
                startAt.format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
