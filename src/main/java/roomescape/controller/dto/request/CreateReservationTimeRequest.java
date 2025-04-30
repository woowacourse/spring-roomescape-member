package roomescape.controller.dto.request;

import java.time.LocalTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
}
