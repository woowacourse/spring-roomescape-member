package roomescape.theme.presentation.dto;

import java.time.LocalTime;
import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeResponse(
        Long id,
        String name,
        String thumbnailImageUrl,
        String description,
        LocalTime durationTime
) {
    public static ThemeResponse from(Theme theme) {
        return ThemeResponse.builder()
                .id(theme.getId())
                .name(theme.getName())
                .thumbnailImageUrl(theme.getThumbnailImageUrl())
                .description(theme.getDescription())
                .durationTime(theme.getDurationTime())
                .build();
    }
}
