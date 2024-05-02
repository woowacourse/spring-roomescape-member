package roomescape.dto;

import java.time.LocalTime;

public record TimeRequest(
        Long id,
        LocalTime startAt
) {
}
