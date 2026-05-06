package roomescape.dto.theme;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.Theme;

public record PopularThemesResponseDto(
    List<PopularThemeResponseDto> themes
) {

    public static PopularThemesResponseDto from(List<Theme> themes) {
        List<PopularThemeResponseDto> popularThemes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            popularThemes.add(
                PopularThemeResponseDto.from(themes.get(i), i + 1));
        }
        return new PopularThemesResponseDto(popularThemes);
    }
}
