package roomescape.theme.controller.dto;

import roomescape.theme.service.PopularTheme;

import java.util.List;

public record PopularThemesResponse(
        List<PopularThemeResponse> themes
) {

    public static PopularThemesResponse from(List<PopularTheme> popularThemes) {
        return new PopularThemesResponse(popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList()
        );
    }
}
