package roomescape.theme.controller.dto;

import roomescape.theme.service.TimeAvailability;

import java.util.List;

public record ThemeAvailableTimesResponse(
        List<ThemeAvailableTimeResponse> times
) {
    public static ThemeAvailableTimesResponse from(List<TimeAvailability> timeAvailabilities) {
        return new ThemeAvailableTimesResponse(timeAvailabilities.stream()
                .map(ThemeAvailableTimeResponse::from)
                .toList()
        );
    }
}
