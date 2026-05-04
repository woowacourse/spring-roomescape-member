package roomescape.dto.theme;

import java.util.List;
import roomescape.domain.ReservationTheme.ReservationTheme;

public record AllThemeResponse(List<ReservationTheme> themes) {
}
