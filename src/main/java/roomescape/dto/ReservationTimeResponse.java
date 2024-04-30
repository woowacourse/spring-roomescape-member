package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
//        String startAt = reservationTime.getStartAt().format(FORMATTER);
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString());
    }
}
