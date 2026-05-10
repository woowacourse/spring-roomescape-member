package roomescape.theme.controller.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.theme.service.dto.ThemeTimeAvailability;

public record ThemeTimeAvailabilityResponse(
        long id,
        String startAt,
        boolean isAvailable
) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private static String formatStartAt(LocalTime time) {
        return time.format(formatter);
    }

    public static ThemeTimeAvailabilityResponse from(ThemeTimeAvailability availability) {
        return new ThemeTimeAvailabilityResponse(
                availability.id(),
                formatStartAt(availability.startAt()),
                availability.isAvailable()
        );
    }

}
