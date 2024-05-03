package roomescape.controller.time;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record AvailabilityTimeResponse(Long id, String startAt, Boolean booked) {

    public static AvailabilityTimeResponse from(final ReservationTime time, final Boolean booked) {
        return new AvailabilityTimeResponse(
                time.getId(),
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                booked
        );
    }
}
