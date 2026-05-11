package roomescape.controller.dto.reservationtime;

import java.util.List;
import roomescape.controller.dto.theme.ThemeResponse;

public record AvailableReservationTimesResponse(
        ThemeResponse theme,
        List<AvailableReservationTimeResponse> availableTimes
) {
    public static AvailableReservationTimesResponse from(ThemeResponse theme,
                                                         List<AvailableReservationTimeResponse> availableTimes) {
        return new AvailableReservationTimesResponse(theme, availableTimes);
    }
}
