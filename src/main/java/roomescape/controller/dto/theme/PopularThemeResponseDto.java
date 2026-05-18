package roomescape.controller.dto.theme;

import roomescape.domain.Theme;

public record PopularThemeResponseDto(
        Long id,
        int rank,
        String name,
        String description,
        String imageUrl
) {
    public static PopularThemeResponseDto of(int rank, Theme theme) {
        return new PopularThemeResponseDto(
                theme.getId(),
                rank,
                theme.getName().value(),
                theme.getDescription(),
                theme.getImageUrl().value());
    }
}
