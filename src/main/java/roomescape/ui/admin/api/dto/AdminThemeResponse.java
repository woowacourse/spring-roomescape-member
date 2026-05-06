package roomescape.ui.admin.api.dto;

import roomescape.service.result.ThemeRegisterResult;

public record AdminThemeResponse(
        long id,
        String name,
        String description,
        String thumbnailImageUrl
) {
    public static AdminThemeResponse from(ThemeRegisterResult result) {
        return new AdminThemeResponse(result.id(), result.name(), result.description(), result.thumbnailImageUrl());
    }
}
