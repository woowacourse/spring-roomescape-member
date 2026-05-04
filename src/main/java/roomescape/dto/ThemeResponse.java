package roomescape.dto;

import roomescape.domain.Theme;

import java.time.Duration;
import java.time.LocalTime;

public record ThemeResponse (
        Long id,
        String name,
        String description,
        String imageUrl,
        LocalTime startAt,
        LocalTime finishAt,
        Duration playTime
) {

    public static ThemeResponse from(Theme saved) {
        return new ThemeResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getStartAt(),
                saved.getFinishAt(),
                saved.getPlayTime()
        );
    }
}

