package roomescape.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.TimeAvailability;

public record AvailableTimeResponse(
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        Boolean isAvailable
) {
        public static AvailableTimeResponse from(TimeAvailability availability) {
                return new AvailableTimeResponse(
                        availability.time(),
                        availability.available()
                );
        }
}
