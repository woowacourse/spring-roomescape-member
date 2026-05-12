package roomescape.service.dto.reservationtime;

import java.util.List;
import roomescape.service.dto.theme.ThemeResult;

public record AvailableReservationTimesResult(
        ThemeResult theme,
        List<AvailableReservationTimeResult> availableTimes
) {
}
