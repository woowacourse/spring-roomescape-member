package roomescape.dto.theme;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.Theme;

public record PopularThemesResponseDto(
    List<PopularThemeResponseDto> themes
) {

    // TODO: 가독성을 위한 메서드 분리?
    public static PopularThemesResponseDto from(List<Theme> themes) {
        List<PopularThemeResponseDto> popularThemes = new ArrayList<>();

        for (int i = 0; i < themes.size(); i++) {
            popularThemes.add(
                PopularThemeResponseDto.from(themes.get(i), i + 1));
        }

        return new PopularThemesResponseDto(popularThemes);
    }
}
