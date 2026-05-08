package roomescape.dto.response;

import roomescape.domain.Theme;

import java.util.List;

public record ThemeResponse(long id, String name, String description, String thumbnailUrl) {
    public static List<ThemeResponse> from(List<Theme> themes) {
        return themes.stream().map(ThemeResponse::from).toList();
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnailUrl());
    }
}
