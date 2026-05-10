package roomescape.admin.theme.dto;

import roomescape.user.theme.Theme;

public record AdminThemeResponse(
        Long id,
        String name,
        String description,
        String imageUrl
) {

    public static AdminThemeResponse from(Theme saved) {
        return new AdminThemeResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getImageUrl()
        );
    }
}