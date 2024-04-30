package roomescape.controller.time;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record TimeResponse(Long id, String startAt) {

    public static TimeResponse from(ReservationTime time) {
        return new TimeResponse(
                time.id(),
                time.startAt().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
