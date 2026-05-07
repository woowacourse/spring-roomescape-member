package roomescape.dto.theme;

import java.util.List;
import java.util.stream.IntStream;
import roomescape.domain.Theme;

public record PopularThemesResponseDto(
    List<PopularThemeResponseDto> themes
) {

    public static PopularThemesResponseDto from(List<Theme> themes) {
        List<PopularThemeResponseDto> ranked = IntStream.range(0, themes.size())
            .mapToObj(i -> PopularThemeResponseDto.from(themes.get(i), i + 1))
            .toList();
        return new PopularThemesResponseDto(ranked);
    }
}
