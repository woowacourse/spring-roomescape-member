package roomescape.theme.dto;

import roomescape.theme.service.AvailableTime;

import java.time.LocalTime;

public record ResponseThemeAvailableTime(
        Long id,
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
