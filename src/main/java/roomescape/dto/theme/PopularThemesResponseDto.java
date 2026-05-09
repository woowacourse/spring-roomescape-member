package roomescape.dto.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import roomescape.domain.Theme;

public record PopularThemesResponseDto(
    List<PopularThemeResponseDto> themes
) {

    public static PopularThemesResponseDto from(Map<Theme, Integer> themesWithRank) {
        return new PopularThemesResponseDto(themesWithRank.keySet()
                .stream()
                .map(theme -> PopularThemeResponseDto.from(theme, themesWithRank.get(theme)))
                .toList());
    }
}
