package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.Theme;

import java.util.List;

public record ThemeFindResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static List<ThemeFindResponse> of(List<Theme> themes) {
        return themes.stream()
                .map(theme -> new ThemeFindResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnailUrl())
                )
                .toList();
    }
}
