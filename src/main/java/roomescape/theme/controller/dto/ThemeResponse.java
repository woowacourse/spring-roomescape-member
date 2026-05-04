package roomescape.theme.controller.dto;

import roomescape.theme.domain.Theme;

public record ThemeResponse(Long id, String description, String thumbnailUrl) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getDescription(), theme.getThumbnailUrl());
    }
}
