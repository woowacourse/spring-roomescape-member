package roomescape.reservation.entity;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.entity.ReservationTimeEntity;

public record ReservationEntity(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeEntity timeEntity
) {
    public ReservationEntity(final Long id, final String name, final String date,
                             final ReservationTimeEntity timeEntity) {
        this(id, name, LocalDate.parse(date), timeEntity);
    }

    public Reservation toReservation() {
        return Reservation.of(id, name, date, timeEntity.toReservationTime());
    }
}