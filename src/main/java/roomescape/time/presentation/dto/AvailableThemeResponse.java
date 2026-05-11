package roomescape.time.presentation.dto;

import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record AvailableThemeResponse(
        Long id,
        String name
) {
    public static AvailableThemeResponse from(Theme theme) {
        return AvailableThemeResponse.builder()
                .id(theme.getId())
                .name(theme.getName())
                .build();
    }
}
