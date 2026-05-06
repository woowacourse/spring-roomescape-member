package roomescape.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record AvailableTimeResponse(
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        Boolean isAvailable
) {
}
