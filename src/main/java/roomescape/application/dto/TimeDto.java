package roomescape.application.dto;

import java.time.LocalTime;

public record TimeDto(
        Long id,
        LocalTime startAt
) {
}
