package roomescape.controller.dto;

import java.util.List;

public record AvailableReservationTimesResponse(
        ThemeResponse theme,
        List<AvailableReservationTimeResponse> availableTimes
) {
    public static AvailableReservationTimesResponse from(ThemeResponse theme,
                                                         List<AvailableReservationTimeResponse> availableTimes) {
        return new AvailableReservationTimesResponse(theme, availableTimes);
    }
}
