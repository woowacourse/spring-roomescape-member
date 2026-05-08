package roomescape.time.presentation.dto;

import java.util.List;
import lombok.Builder;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeResponse(
        AvailableThemeResponse theme,
        List<AvailableReservationTimeDto> times
) {
    public static AvailableReservationTimeResponse from(Theme theme, List<ReservationTime> times) {
        return AvailableReservationTimeResponse.builder()
                .theme(AvailableThemeResponse.from(theme))
                .times(times.stream()
                        .map(AvailableReservationTimeDto::from)
                        .toList())
                .build();
    }

}
