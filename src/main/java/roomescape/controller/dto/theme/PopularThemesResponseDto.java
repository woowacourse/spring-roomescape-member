package roomescape.controller.dto.theme;

import roomescape.domain.Theme;

import java.util.List;
import java.util.stream.IntStream;

public record PopularThemesResponseDto(
        List<PopularThemeResponseDto> themes
) {

    public static PopularThemesResponseDto from(List<Theme> themesOrderedByRank) {
        return new PopularThemesResponseDto(IntStream.range(0, themesOrderedByRank.size())
                .mapToObj(i -> PopularThemeResponseDto.of(i + 1, themesOrderedByRank.get(i)))
                .toList());
    }
}
