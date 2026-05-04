package roomescape.theme.presentation.dto;

import java.time.LocalTime;
import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeRequest(
        String name,
        String thumbnailImageUrl,
        String description,
        LocalTime durationTime
) {
    public static Theme toEntity(ThemeRequest request) {
        return Theme.builder()
                .name(request.name)
                .description(request.description)
                .thumbnailImageUrl(request.thumbnailImageUrl)
                .durationTime(request.durationTime)
                .build();
    }
}
