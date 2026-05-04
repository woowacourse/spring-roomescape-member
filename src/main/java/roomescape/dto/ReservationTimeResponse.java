package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {
    public ReservationTimeResponse(ReservationTime reservationTime) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
