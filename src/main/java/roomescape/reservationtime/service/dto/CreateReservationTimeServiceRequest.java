package roomescape.reservationtime.service.dto;

import java.time.LocalTime;

public record CreateReservationTimeServiceRequest(LocalTime startAt) {
}
