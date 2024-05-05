package roomescape.service.dto;

import roomescape.domain.Theme;

import java.util.List;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public ThemeResponse(Theme theme) {
        this(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static List<ThemeResponse> listOf(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
