package roomescape.theme.dto.response;

import roomescape.theme.domain.Theme;

public record ThemeSelectElementResponse(Long id, String name) {

    public static ThemeSelectElementResponse from(Theme theme) {
        return new ThemeSelectElementResponse(theme.getId(), theme.getName());
    }
}
