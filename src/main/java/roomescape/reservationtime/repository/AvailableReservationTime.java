package roomescape.reservationtime.repository;

import java.time.LocalTime;

public record AvailableReservationTime(Long id, LocalTime startAt, boolean available) {
}
