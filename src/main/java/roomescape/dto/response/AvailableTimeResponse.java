package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;

public record AvailableTimeResponse(@JsonProperty("timeId") Long timeId, @JsonProperty("startAt") LocalTime startAt,
                                    @JsonProperty("alreadyBooked") Boolean alreadyBooked) {
}
