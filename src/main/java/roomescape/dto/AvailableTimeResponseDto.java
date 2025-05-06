package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;

public record AvailableTimeResponseDto(@JsonProperty("timeId") Long timeId, @JsonProperty("startAt") LocalTime startAt,
                                       @JsonProperty("alreadyBooked") Boolean alreadyBooked) {
}
