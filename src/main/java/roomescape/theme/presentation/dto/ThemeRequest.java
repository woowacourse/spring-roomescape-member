package roomescape.theme.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeRequest(
        @NotBlank
        String name,
        @NotBlank
        String thumbnailImageUrl,
        @NotBlank
        String description,
        @NotNull
        LocalTime durationTime
) {
    public Theme toEntity() {
        return Theme.builder()
                .name(name)
                .description(description)
                .thumbnailImageUrl(thumbnailImageUrl)
                .durationTime(durationTime)
                .build();
    }
}
