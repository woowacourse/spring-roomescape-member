package roomescape.schedule;

import java.time.LocalTime;

public record AvailableTimeDto(long timeId, LocalTime startAt, boolean isAvailable) {

}
