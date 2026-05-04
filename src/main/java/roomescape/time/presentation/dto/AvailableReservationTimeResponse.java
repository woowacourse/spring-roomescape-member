package roomescape.time.presentation.dto;

import java.util.List;
import lombok.Builder;
import roomescape.theme.domain.Theme;
import roomescape.theme.presentation.dto.ThemeResponse;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeResponse(
        ThemeResponse theme,
        List<ReservationTimeResponse> times
) {
    public static AvailableReservationTimeResponse from(Theme theme, List<ReservationTime> times) {
        return AvailableReservationTimeResponse.builder()
                .times(times.stream().map(ReservationTimeResponse::from).toList())
                .theme(ThemeResponse.from(theme))
                .build();
    }
}
