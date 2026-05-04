package roomescape.time.dto;

import java.time.LocalTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
}
