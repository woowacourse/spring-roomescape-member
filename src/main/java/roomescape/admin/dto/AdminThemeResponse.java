package roomescape.admin.dto;

import roomescape.domain.Theme;

public record AdminThemeResponse(
        Long id,
        String name,
        String description,
        String image
) {
    public static AdminThemeResponse from(Theme theme) {
        return new AdminThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getImage());
    }
}
