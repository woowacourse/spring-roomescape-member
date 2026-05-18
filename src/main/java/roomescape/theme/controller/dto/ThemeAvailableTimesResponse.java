package roomescape.theme.controller.dto;

import roomescape.theme.service.AvailableTimes;
import roomescape.theme.service.TimeAvailability;

import java.util.List;

public record ThemeAvailableTimesResponse(
        List<ThemeAvailableTimeResponse> times
) {
    public static ThemeAvailableTimesResponse from(AvailableTimes availableTimes) {
        return new ThemeAvailableTimesResponse(toResponses(availableTimes.values()));
    }

    private static List<ThemeAvailableTimeResponse> toResponses(List<TimeAvailability> timeAvailabilities) {
        return timeAvailabilities.stream()
                .map(ThemeAvailableTimeResponse::from)
                .toList();
    }
}
