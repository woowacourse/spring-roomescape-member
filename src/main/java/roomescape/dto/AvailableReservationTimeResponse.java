package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record AvailableReservationTimeResponse(
        Long id,
        String startAt,
        boolean isReserved
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static AvailableReservationTimeResponse of(ReservationTime reservationTime, boolean isReserved) {
        String startAt = reservationTime.getStartAt().format(FORMATTER);
        return new AvailableReservationTimeResponse(reservationTime.getId(), startAt, isReserved);
    }
}
