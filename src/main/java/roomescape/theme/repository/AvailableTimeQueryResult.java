package roomescape.theme.repository;

import java.time.LocalTime;

public record AvailableTimeQueryResult(Long id, LocalTime startAt) {
}
