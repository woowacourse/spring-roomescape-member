package roomescape.dto.response;

import roomescape.model.Theme;

public record BriefThemeResponse(Long id, String name) {

    public static BriefThemeResponse from(Theme theme) {
        return new BriefThemeResponse(theme.getId(), theme.getName());
    }
}
