package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
}
