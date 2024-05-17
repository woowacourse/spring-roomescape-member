package roomescape.reservation.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record AvailableTimeResponse(Long timeId, String startAt, Boolean alreadyBooked) {
    public static AvailableTimeResponse from(Long timeId, LocalTime startAt, Boolean alreadyBooked) {
        return new AvailableTimeResponse(
                timeId,
                startAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                alreadyBooked
        );
    }
}
