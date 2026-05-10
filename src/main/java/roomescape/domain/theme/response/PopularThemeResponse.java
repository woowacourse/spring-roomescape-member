package roomescape.domain.theme.response;

import roomescape.domain.theme.repository.PopularThemeResult;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        Integer rank
) {

    public static PopularThemeResponse from(PopularThemeResult result) {
        return new PopularThemeResponse(
                result.id(),
                result.name(),
                result.description(),
                result.thumbnailUrl(),
                result.rank()
        );
    }
}
