package roomescape.theme.dto;

import roomescape.theme.service.PopularTheme;

public record ResponsePopularTheme(
        Long id,
        String name,
        String description,
        String thumbnail,
        int rank
) {
    public static ResponsePopularTheme from(PopularTheme popularTheme) {
        return new ResponsePopularTheme(
                popularTheme.id(),
                popularTheme.name(),
                popularTheme.description(),
                popularTheme.thumbnail(),
                popularTheme.rank()
        );
    }
}
