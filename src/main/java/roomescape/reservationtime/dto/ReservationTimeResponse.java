package roomescape.reservationtime.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(
                time.getId(),
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }

    public static ReservationTimeResponse from(Long id, LocalTime startAt) {
        return new ReservationTimeResponse(
                id,
                startAt.format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
