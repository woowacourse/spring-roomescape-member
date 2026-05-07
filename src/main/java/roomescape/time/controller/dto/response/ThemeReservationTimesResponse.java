package roomescape.time.controller.dto.response;

import java.util.List;
import roomescape.theme.controller.dto.ThemeResponse;

public record ThemeReservationTimesResponse(
        ThemeResponse theme,
        List<AvailableReservationTimeResponse> availableTimes
) {
    public static ThemeReservationTimesResponse from(ThemeResponse theme, List<AvailableReservationTimeResponse> availableTimes) {
        return new ThemeReservationTimesResponse(theme, availableTimes);
    }
}
