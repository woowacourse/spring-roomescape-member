package roomescape.dto;

import java.time.LocalTime;

public record TimeSaveRequest(
        Long id,
        LocalTime startAt
) {
}
