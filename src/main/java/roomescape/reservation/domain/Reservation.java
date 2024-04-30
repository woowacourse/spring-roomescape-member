package roomescape.reservation.domain;

import java.time.LocalDate;

import roomescape.time.domain.ReservationTime;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {
}
