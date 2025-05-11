package roomescape.theme.dto.response;

import roomescape.theme.domain.Theme;

public record BriefThemeResponse(Long id, String name) {

    public static BriefThemeResponse from(Theme theme) {
        return new BriefThemeResponse(theme.getId(), theme.getName());
    }
}
