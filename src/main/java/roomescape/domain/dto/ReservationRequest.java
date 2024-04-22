package roomescape.domain.dto;

import roomescape.domain.Reservation;
import roomescape.domain.TimeSlot;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId) {
    public Reservation toEntity(Long id, TimeSlot time) {
        return new Reservation(id, name, date, time);
    }
}
