package roomescape.controller.dto;

import java.util.List;

public record ThemeReservationTimesResponse(
        ThemeResponse theme,
        List<AvailableReservationTimeResponse> availableTimes
) {
    public static ThemeReservationTimesResponse from(ThemeResponse theme, List<AvailableReservationTimeResponse> availableTimes) {
        return new ThemeReservationTimesResponse(theme, availableTimes);
    }
}
