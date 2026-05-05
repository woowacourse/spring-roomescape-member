package roomescape.time.application.dto;

import java.util.List;
import lombok.Builder;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeInfo(
        Theme theme,
        List<ReservationTime> times
) {
    public static AvailableReservationTimeInfo from(Theme theme, List<ReservationTime> times) {
        return AvailableReservationTimeInfo.builder()
                .theme(theme)
                .times(times)
                .build();
    }
}
