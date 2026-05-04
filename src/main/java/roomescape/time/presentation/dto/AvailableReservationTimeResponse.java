package roomescape.time.presentation.dto;

import java.util.List;
import lombok.Builder;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeResponse(
        Theme theme,
        List<ReservationTime> times
) {
    public static AvailableReservationTimeResponse from(Theme theme, List<ReservationTime> times) {
        return AvailableReservationTimeResponse.builder()
                .times(times)
                .theme(theme)
                .build();
    }
}
