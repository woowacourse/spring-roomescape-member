package roomescape.controller.dto.reservationtime;

import java.util.List;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.service.dto.AvailableReservationTimesResult;

public record AvailableReservationTimesResponse(
        ThemeResponse theme,
        List<AvailableReservationTimeResponse> availableTimes
) {
    public static AvailableReservationTimesResponse from(ThemeResponse theme,
                                                         List<AvailableReservationTimeResponse> availableTimes) {
        return new AvailableReservationTimesResponse(theme, availableTimes);
    }

    public static AvailableReservationTimesResponse from(AvailableReservationTimesResult result) {
        List<AvailableReservationTimeResponse> availableTimes = result.availableTimes().stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
        return new AvailableReservationTimesResponse(ThemeResponse.from(result.theme()), availableTimes);
    }
}
