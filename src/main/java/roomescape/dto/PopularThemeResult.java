package roomescape.dto;

import roomescape.domain.Theme;

public record PopularThemeResult(
        Theme theme,
        int reservationCount
) {
}
