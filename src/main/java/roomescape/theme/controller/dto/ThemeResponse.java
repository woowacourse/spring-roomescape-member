package roomescape.theme.controller.dto;

import roomescape.theme.service.dto.ThemeResult;

public record ThemeResponse(Long id, String name, String description, String thumbnailUrl) {

    public static ThemeResponse from(ThemeResult theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnailUrl());
    }
}
