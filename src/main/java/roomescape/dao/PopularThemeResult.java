package roomescape.dao;

import roomescape.domain.Theme;

public record PopularThemeResult(
        Theme theme,
        int reservationCount
) {
}
