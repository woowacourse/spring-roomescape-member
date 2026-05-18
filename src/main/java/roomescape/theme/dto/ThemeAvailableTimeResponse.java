package roomescape.theme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.theme.service.AvailableTime;

import java.time.LocalTime;

public record ThemeAvailableTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {
    public static ThemeAvailableTimeResponse from(AvailableTime availableTime) {
        return new ThemeAvailableTimeResponse(
                availableTime.id(),
                availableTime.time(),
                availableTime.isAvailable()
        );
    }
}
