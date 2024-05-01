package roomescape.reservation.domain;

import java.time.LocalDate;

import roomescape.time.domain.ReservationTime;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {
    public Reservation {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("Name cannot exceed 10 characters");
        }
    }
}
