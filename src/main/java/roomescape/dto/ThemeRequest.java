package roomescape.dto;

import java.time.Duration;
import java.time.LocalTime;

public record ThemeRequest (
        String name,
        String description,
        String imageUrl,
        LocalTime startAt,
        LocalTime finishAt,
        Duration playTime
){
}
