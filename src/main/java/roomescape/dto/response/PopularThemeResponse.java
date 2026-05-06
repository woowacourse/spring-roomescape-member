package roomescape.dto.response;

import java.util.List;
import roomescape.dto.PopularTheme;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String imgUrl,
        Long rank,
        Long reservationCount
) {
    public static List<PopularThemeResponse> toDto(List<PopularTheme> popularThemes) {
        return popularThemes.stream()
                .map(
                        popularTheme -> new PopularThemeResponse(
                                popularTheme.id(),
                                popularTheme.name(),
                                popularTheme.description(),
                                popularTheme.imgUrl(),
                                popularTheme.rank(),
                                popularTheme.reservationCount()
                        )
                ).toList();
    }
}
