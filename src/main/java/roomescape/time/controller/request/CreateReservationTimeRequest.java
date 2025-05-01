package roomescape.time.controller.request;

import java.time.LocalTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
}
