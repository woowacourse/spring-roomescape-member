package roomescape.time.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record AvailableTimeResponse(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt,
        Long timeId,
        Boolean alreadyBooked
) {

    public static AvailableTimeResponse from(LocalTime startAt, Long timeId, Boolean alreadyBooked) {
        return new AvailableTimeResponse(startAt, timeId, alreadyBooked);
    }

}
