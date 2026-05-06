package roomescape.reservationtime;

import java.time.LocalTime;

public record AvailableTimeDto(long timeId, LocalTime startAt, boolean isAvailable) {

}
