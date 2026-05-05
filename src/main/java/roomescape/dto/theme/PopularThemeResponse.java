package roomescape.dto.theme;

import java.util.List;
import roomescape.domain.ReservationTheme.ReservationThemeWithCount;

public record PopularThemeResponse(List<ReservationThemeWithCount> popularThemes) {
    public static PopularThemeResponse from(List<ReservationThemeWithCount> popularThemes) {
        return new PopularThemeResponse(popularThemes);
    }
}
