package roomescape.theme.controller.dto;

import roomescape.theme.doamin.Theme;

public record ThemeResponse (Long id, String name, String description, String thumbnailUrl){

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }
}
