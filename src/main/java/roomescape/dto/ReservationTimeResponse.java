package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
