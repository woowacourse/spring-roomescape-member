package roomescape.theme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.theme.service.AvailableTime;

import java.time.LocalTime;

public record ResponseThemeAvailableTime(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {
    public static ResponseThemeAvailableTime from(AvailableTime availableTime) {
        return new ResponseThemeAvailableTime(
                availableTime.id(),
                availableTime.time(),
                availableTime.isAvailable()
        );
    }
}
