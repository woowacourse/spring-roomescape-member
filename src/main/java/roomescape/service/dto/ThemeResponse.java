package roomescape.service.dto;

import roomescape.domain.Theme;

import java.util.List;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeResponse of(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }

    public static List<ThemeResponse> listOf(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::of)
                .toList();
    }
}
