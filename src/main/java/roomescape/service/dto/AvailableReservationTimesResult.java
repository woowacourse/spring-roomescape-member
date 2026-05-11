package roomescape.service.dto;

import java.util.List;

public record AvailableReservationTimesResult(
        ThemeResult theme,
        List<AvailableReservationTimeResult> availableTimes
) {
}
